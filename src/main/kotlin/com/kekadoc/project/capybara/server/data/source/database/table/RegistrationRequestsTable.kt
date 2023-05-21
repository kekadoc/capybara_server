package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object RegistrationRequestsTable : UUIDTable("reg_request") {

    val name = varchar("name", 64)
    val surname = varchar("surname", 64)
    val patronymic = varchar("patronymic", 64)
    val email = varchar("email", 64)
    val emailApproved = bool("email_approved").default(false)
    val adminApproved = bool("admin_approved").default(false)
    val isStudent = bool("is_student").default(false)
    val group = uuid("group").nullable().default(null)
    val status = text("status")

}