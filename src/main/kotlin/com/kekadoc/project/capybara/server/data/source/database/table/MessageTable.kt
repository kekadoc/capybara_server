package com.kekadoc.project.capybara.server.data.source.database.table

import com.kekadoc.project.capybara.server.data.source.database.utils.textArray
import com.kekadoc.project.capybara.server.domain.model.message.MessageStatus
import com.kekadoc.project.capybara.server.domain.model.message.MessageType
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption

object MessageTable : UUIDTable("notifications") {

    val author = reference(
        name = "author",
        foreign = UsersTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )

    val type = varchar("type", 255)
        .default(MessageType.DEFAULT.name)

    val date = text("date")

    val contentTitle = varchar("content_title", 255)
        .nullable()
        .default(null)

    val contentText = varchar("content_text", 255)

    val status: Column<String> = varchar("status", 255)
        .default(MessageStatus.UNDEFINED.name)

    val isMultiAnswer = bool("is_multi_answer")
        .default(false)

    val actions = textArray("actions")
        .nullable()
        .default(null)

    val notificationEmail = bool("notification_email")
        .default(false)

    val notificationSms = bool("notification_sms")
        .default(false)

    val notificationApp = bool("notification_app")
        .default(false)

    val notificationMessengers = bool("notification_messengers")
        .default(false)

}