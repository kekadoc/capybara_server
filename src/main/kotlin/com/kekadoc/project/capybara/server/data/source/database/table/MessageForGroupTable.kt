package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object MessageForGroupTable : UUIDTable("messages_for_groups") {

    val message_id = reference(
        name = "message_id",
        foreign = MessageTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val groupId = reference(
        name = "group_id",
        foreign = GroupsTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )

}