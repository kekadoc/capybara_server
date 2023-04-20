package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

//
object NotificationsForUsersTable : UUIDTable("notification_users") {

    val notificationId = reference("notification_id", NotificationsTable)
    val userId = reference("user_id", UsersTable)
    val received = bool("received")
    val read = bool("read")
    val answer = bool("answer")

}