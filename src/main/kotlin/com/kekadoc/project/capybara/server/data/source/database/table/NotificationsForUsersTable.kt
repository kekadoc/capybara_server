package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object NotificationsForUsersTable : UUIDTable("notification_users") {

    val notificationId = reference("notification_id", NotificationsTable, onDelete = ReferenceOption.CASCADE)
    val userId = reference("user_id", UsersTable, onDelete = ReferenceOption.CASCADE)
    val received = bool("received").default(false)
    val read = bool("read").default(false)
    val answer = text("answer").nullable().default(null)
    val fromGroup = bool("fromGroup").default(false)

}