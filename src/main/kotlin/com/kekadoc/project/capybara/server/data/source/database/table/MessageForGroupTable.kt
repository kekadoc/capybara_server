package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object MessageForGroupTable : UUIDTable("messages_for_groups") {

    val message_id = reference("message_id", MessageTable)
    val groupId = reference("group_id", GroupsTable)

}