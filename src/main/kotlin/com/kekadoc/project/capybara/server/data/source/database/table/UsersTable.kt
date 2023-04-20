package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

//
object UsersTable : UUIDTable("users") {

    val login = varchar("login", 255)
    val password = varchar("password", 255)
    val profile = reference("profile", ProfilesTable)
    val character = reference("character", ProfilesTable)

}