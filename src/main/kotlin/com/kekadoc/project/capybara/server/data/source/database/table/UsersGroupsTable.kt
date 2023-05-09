package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object UsersGroupsTable : UUIDTable("users_groups") {

    val user = reference("user", UsersTable)
    val group = reference("group", GroupsTable)

}