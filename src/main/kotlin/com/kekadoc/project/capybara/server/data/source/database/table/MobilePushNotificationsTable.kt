package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object MobilePushNotificationsTable : UUIDTable("mobile_push_notifications") {

    val user = reference(
        name = "user",
        foreign = UsersTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val notification = reference(
        name = "notification",
        foreign = MessageTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val pushId = text("push_id")

}