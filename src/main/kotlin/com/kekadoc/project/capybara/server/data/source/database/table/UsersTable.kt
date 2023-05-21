package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object UsersTable : UUIDTable("users") {

    val profile = reference(
        name = "profile",
        foreign = ProfilesTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val status = text("status")
    val login = varchar("login", 255)
    val password = varchar("password", 255)

}