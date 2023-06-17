package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object GroupsTable : UUIDTable("groups") {

    val name = varchar("name", 255)
    val type = varchar("type", 64)

}