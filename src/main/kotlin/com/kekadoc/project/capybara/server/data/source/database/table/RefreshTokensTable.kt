package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object RefreshTokensTable : UUIDTable("refresh_tokens") {

    val token = varchar("token", 256)
    val expiresAt = long("expires_at")
    val userId = uuid("user_id")

}