package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object CommunicationsTable : UUIDTable("communications") {

    val user = reference(
        name = "user", UsersTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val type = varchar("type", 256)
    val value = varchar("value", 256)
    val approved = bool("approved").nullable().default(null)

}