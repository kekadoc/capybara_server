package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column

//
object NotificationsTable : UUIDTable("notifications") {

    val author = reference("author", UsersTable)
    val type = varchar("type", 255)
    val contentTitle: Column<String> = varchar("content_title", 255)
    val contentText: Column<String> = varchar("content_text", 255)
    val contentImage: Column<String> = varchar("content_image", 255)
    val status: Column<String> = varchar("status", 255)

}