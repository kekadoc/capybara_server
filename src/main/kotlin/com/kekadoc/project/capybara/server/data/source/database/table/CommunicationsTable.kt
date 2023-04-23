package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object CommunicationsTable : UUIDTable("communications") {

    val user = reference("user", UsersTable)
    val type = varchar("type", 256)
    val value = varchar("value", 256)

}