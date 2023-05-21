package com.kekadoc.project.capybara.server.data.source.api.auth

import com.kekadoc.project.capybara.server.common.exception.EntityNotFoundException
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.source.api.notifications.email.EmailNotificationConfig
import com.kekadoc.project.capybara.server.data.source.database.entity.RegistrationRequestEntity
import com.kekadoc.project.capybara.server.data.source.database.table.RegistrationRequestsTable
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.auth.registration.*
import io.ktor.http.*
import io.ktor.utils.io.charsets.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.charset.StandardCharsets

class RegistrationDataSourceImpl(
    private val config: EmailNotificationConfig,
) : RegistrationDataSource {

    private val host = if (com.kekadoc.project.capybara.server.Application.isDebug) {
        "http://localhost:8080/api/v1/auth/registration"
    } else {
        "https://capybara-server.onrender.com/api/v1/auth/registration"
    }

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
        sentConfirmEmail(newRequest.id.value, email = request.email)
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
        println("______!!!_____${request.status}")
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
        println("______!!!_____${request.status}")
        val requestInfo = RegistrationRequestInfo(
            id = entity.id.value,
            status = RegistrationStatus.valueOf(entity.status),
            name = entity.name,
            surname = entity.surname,
            patronymic = entity.patronymic,
            email = entity.email,
            isStudent = entity.isStudent,
            groupId = entity.group,
        )
        if (request.status == RegistrationStatus.COMPLETED) {
            sentEmailWithLoginEndTempPassword(requestInfo)
        }
        requestInfo
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

    private fun sentConfirmEmail(
        registrationId: Identifier,
        email: String,
    ) {
        val notificationText = buildConfirmEmailMessage(
            head = config.subject,
            title = "Подтверждение почты",
            message = "Нажмите что бы подтвердить почту",
            action1 = "Подтвердить" to createConfirmRequestUrl(registrationId),
        )
        HtmlEmail().apply {
            hostName = config.hostName
            setSmtpPort(465)
            setAuthenticator(DefaultAuthenticator(config.username, config.password))
            isSSLOnConnect = true
            setFrom(config.fromEmail)
            subject = config.subject
            setHtmlMsg(notificationText)
            setCharset(StandardCharsets.UTF_8.name)
            addTo(email)
        }.send()
    }

    private fun sentEmailWithLoginEndTempPassword(
        requestInfo: RegistrationRequestInfo,
    ) {
        val notificationText = buildEmailWithTempPasswordAndLogin(
            requestInfo,
        )
        HtmlEmail().apply {
            hostName = config.hostName
            setSmtpPort(465)
            setAuthenticator(DefaultAuthenticator(config.username, config.password))
            isSSLOnConnect = true
            setFrom(config.fromEmail)
            subject = config.subject
            setHtmlMsg(notificationText)
            setCharset(StandardCharsets.UTF_8.name)
            addTo(requestInfo.email)
        }.send()
    }

    private fun createConfirmRequestUrl(
        registrationId: Identifier,
    ): String {
        return "$host/$registrationId/confirm_email"
    }

    private fun buildEmailWithTempPasswordAndLogin(
        requestInfo: RegistrationRequestInfo,
        //head: String,
    ): String = buildString {
        append("<!DOCTYPE html>\n")
        appendHTML().html {
            lang = "ru"
            head {
                meta {
                    httpEquiv = "Content-Type"
                    content = "text/html"
                    charset = "utf-8"
                }
                //title(head)
            }
            body {
                attributes["style"] = "font-family: Arial, sans-serif; font-size: 14px;"
                p {
                    text("Здравствуйте, ${requestInfo.name} ${requestInfo.patronymic}!")
                }
                p {
                    text("Вы зарегестрованы в системе. Вам предоставлены логин и временный пароль:\nЛогин: Oleg1234\nПароль: 1234")
                }
            }
        }
    }

    private fun buildConfirmEmailMessage(
        head: String,
        title: String?,
        message: String,
        action1: Pair<String, String>? = null,
        action2: Pair<String, String>? = null,
        action3: Pair<String, String>? = null,
    ): String = buildString {
        append("<!DOCTYPE html>\n")
        appendHTML().html {
            lang = "ru"
            head {
                meta {
                    httpEquiv = "Content-Type"
                    content = "text/html"
                    charset = "utf-8"
                }
                title(head)
            }
            body {
                attributes["style"] = "font-family: Arial, sans-serif; font-size: 14px;"
                if (title != null) {
                    p {
                        text(title)
                    }
                }
                p {
                    text(message)
                }
                if (action1 != null || action2 != null || action3 != null) {
                    table {
                        attributes["border"] = "0"
                        attributes["cellpadding"] = "0"
                        attributes["cellspacing"] = "0"
                        tr {
                            val actions = listOfNotNull(action1, action2, action3)
                            actions.forEachIndexed { index, (text, url) ->
                                td {
                                    attributes["align"] = "center"
                                    attributes["bgcolor"] = "#008CBA"
                                    attributes["style"] = "padding: 10px;"
                                    a(href = url) {
                                        this.attributes["style"] = "color:#ffffff;text-decoration:none;padding:10px 20px;border-radius:3px;"
                                        this.text(text)
                                    }
                                }
                                if (index < actions.size - 1) {
                                    td {
                                        attributes["width"] = "20"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}