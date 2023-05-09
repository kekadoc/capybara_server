package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object PushTokensTable : UUIDTable("push_tokens") {

    val user = reference("user", UsersTable)
    val token = varchar("token", 255)

}