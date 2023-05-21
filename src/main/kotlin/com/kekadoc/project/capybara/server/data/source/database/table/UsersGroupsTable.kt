package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object UsersGroupsTable : UUIDTable("users_groups") {

    val user = reference(
        name = "user",
        foreign = UsersTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val group = reference(
        name = "group",
        foreign = GroupsTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )

}