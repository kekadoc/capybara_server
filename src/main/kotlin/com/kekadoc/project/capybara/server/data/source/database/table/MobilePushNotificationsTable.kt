package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object MobilePushNotificationsTable : UUIDTable("mobile_push_notifications") {

    val user = reference("user", UsersTable)
    val notification = reference("notification", MessageTable)
    val pushId = text("push_id")

}