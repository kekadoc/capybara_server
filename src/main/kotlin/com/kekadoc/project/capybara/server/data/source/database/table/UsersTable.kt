package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object UsersTable : UUIDTable("users") {

    val login = varchar("login", 255)
    val password = varchar("password", 255)
    val type = varchar("type", 255)
    val status = text("status")
    val name = text("name")
    val surname = text("surname")
    val patronymic = text("patronymic").nullable().default(null)
    val about = text("about").nullable().default(null)

}