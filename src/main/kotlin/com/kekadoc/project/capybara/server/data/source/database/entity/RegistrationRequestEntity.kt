package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.RegistrationRequestsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class RegistrationRequestEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<RegistrationRequestEntity>(RegistrationRequestsTable)

    var name by RegistrationRequestsTable.name
    var surname by RegistrationRequestsTable.surname
    var patronymic by RegistrationRequestsTable.patronymic
    var email by RegistrationRequestsTable.email
    var emailApproved by RegistrationRequestsTable.emailApproved
    var adminApproved by RegistrationRequestsTable.adminApproved
    var isStudent by RegistrationRequestsTable.isStudent
    var group by RegistrationRequestsTable.group
    var status by RegistrationRequestsTable.status

}