package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

//
object AuthTokensTable : UUIDTable("auth_tokens") {

    val user = reference("user", UsersTable)
    val token = varchar("token", 255)
    val createdAt = timestamp("createdAt").defaultExpression(CurrentTimestamp())

}