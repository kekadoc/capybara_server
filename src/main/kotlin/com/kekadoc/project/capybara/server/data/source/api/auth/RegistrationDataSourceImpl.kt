package com.kekadoc.project.capybara.server.data.source.api.auth

import com.kekadoc.project.capybara.server.common.exception.EntityNotFoundException
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.source.database.entity.RegistrationRequestEntity
import com.kekadoc.project.capybara.server.data.source.database.table.RegistrationRequestsTable
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.auth.registration.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

class RegistrationDataSourceImpl : RegistrationDataSource {

    override suspend fun registration(
        request: RegistrationRequest,
    ): RegistrationRequestInfo = transaction {
        val isEmailAlreadyExist = RegistrationRequestEntity.find {
            val emailEq = (RegistrationRequestsTable.email eq request.email)
            val isEmailApproved = RegistrationRequestsTable.emailApproved
            val isStatusActive = RegistrationRequestsTable.status inList listOf(
                RegistrationStatus.WAIT_APPROVING,
                RegistrationStatus.WAIT_EMAIL_CONFIRMING,
                RegistrationStatus.COMPLETED,
            ).map(RegistrationStatus::name)
            emailEq and isEmailApproved and isStatusActive
        }.count() > 0
        if (isEmailAlreadyExist) {
            throw HttpException(statusCode = HttpStatusCode.BadRequest, "Email already exist")
        } else {
            RegistrationRequestEntity.find {
                RegistrationRequestsTable.email eq request.email
            }.forEach(RegistrationRequestEntity::delete)
        }
        val newRequest = RegistrationRequestEntity.new {
            this.status = RegistrationStatus.WAIT_EMAIL_CONFIRMING.name
            this.name = request.name
            this.surname = request.surname
            this.patronymic = request.patronymic
            this.email = request.email
            this.group = request.groupId
            this.isStudent = request.isStudent
        }
        RegistrationRequestInfo(
            id = newRequest.id.value,
            status = RegistrationStatus.valueOf(newRequest.status),
            name = newRequest.name,
            surname = newRequest.surname,
            patronymic = newRequest.patronymic,
            email = newRequest.email,
            isStudent = newRequest.isStudent,
            groupId = newRequest.group,
        )
    }

    override suspend fun getRegistrationStatus(
        registrationId: Identifier,
    ): RegistrationRequestInfo = transaction {
        val entity = RegistrationRequestEntity.findById(registrationId)
            ?: throw EntityNotFoundException("Registration request not found, $registrationId")
        RegistrationRequestInfo(
            id = entity.id.value,
            status = RegistrationStatus.valueOf(entity.status),
            name = entity.name,
            surname = entity.surname,
            patronymic = entity.patronymic,
            email = entity.email,
            isStudent = entity.isStudent,
            groupId = entity.group,
        )
    }

    override suspend fun updateRegistrationStatus(
        registrationId: Identifier,
        request: UpdateRegistrationStatusRequest,
    ): RegistrationRequestInfo = transaction {
        val entity = RegistrationRequestEntity.findById(registrationId)
            ?: throw EntityNotFoundException("Registration request not found, $registrationId")
        entity.apply {
            when (request.status) {
                RegistrationStatus.WAIT_EMAIL_CONFIRMING -> {
                    this.emailApproved = false
                    this.adminApproved = false
                }
                RegistrationStatus.WAIT_APPROVING -> {
                    this.emailApproved = true
                    this.adminApproved = false
                }
                RegistrationStatus.COMPLETED -> {
                    this.emailApproved = true
                    this.adminApproved = true
                }
                RegistrationStatus.REJECTED -> {}
                RegistrationStatus.CANCELLED -> {}
            }
            status = request.status.name
        }
        RegistrationRequestInfo(
            id = entity.id.value,
            status = RegistrationStatus.valueOf(entity.status),
            name = entity.name,
            surname = entity.surname,
            patronymic = entity.patronymic,
            email = entity.email,
            isStudent = entity.isStudent,
            groupId = entity.group,
        )
    }

    override suspend fun getAllRegistrationRequests(

    ): GetAllRegistrationRequestsResponse = transaction {
        val items = RegistrationRequestEntity.all().map {
            RegistrationRequestInfo(
                id = it.id.value,
                status = RegistrationStatus.valueOf(it.status),
                name = it.name,
                surname = it.surname,
                patronymic = it.patronymic,
                email = it.email,
                isStudent = it.isStudent,
                groupId = it.group,
            )
        }
        GetAllRegistrationRequestsResponse(items)
    }

}