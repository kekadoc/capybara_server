package com.kekadoc.project.capybara.server.data.source.database.table

import com.kekadoc.project.capybara.server.domain.model.Message
import com.kekadoc.project.capybara.server.domain.model.MessageInfo
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
        .default(Message.Type.DEFAULT.name)

    val contentTitle = varchar("content_title", 255)
        .nullable()
        .default(null)

    val contentText = varchar("content_text", 255)

    val contentImage = varchar("content_image", 255)
        .nullable()
        .default(null)

    val status: Column<String> = varchar("status", 255)
        .default(MessageInfo.Status.UNDEFINED.name)

    val action1 = varchar("action_1", length = 255)
        .nullable()
        .default(null)

    val action2 = varchar("action_2", length = 255)
        .nullable()
        .default(null)

    val action3 = varchar("action_3", length = 255)
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