package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

//
object NotificationsForGroupsTable : UUIDTable("notifications_groups") {

    val notificationId = reference("notification_id", NotificationsTable)
    val group = reference("group", GroupsTable)

}