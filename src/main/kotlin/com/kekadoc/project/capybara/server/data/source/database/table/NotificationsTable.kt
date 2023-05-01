package com.kekadoc.project.capybara.server.data.source.database.table

import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.model.NotificationInfo
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column

object NotificationsTable : UUIDTable("notifications") {

    val author = reference("author", UsersTable)
    val type = varchar("type", 255).default(Notification.Type.DEFAULT.name)
    val contentTitle = varchar("content_title", 255).nullable().default(null)
    val contentText = varchar("content_text", 255)
    val contentImage = varchar("content_image", 255).nullable().default(null)
    val status: Column<String> = varchar("status", 255).default(NotificationInfo.Status.UNDEFINED.name)

}