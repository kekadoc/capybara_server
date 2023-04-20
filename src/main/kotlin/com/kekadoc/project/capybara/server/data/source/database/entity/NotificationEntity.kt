package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.NotificationsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class NotificationEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<NotificationEntity>(NotificationsTable)

    var author by UserEntity referencedOn NotificationsTable.author
    var type by NotificationsTable.type
    var contentTitle by NotificationsTable.contentTitle
    var contentText by NotificationsTable.contentText
    var contentImage by NotificationsTable.contentImage
    var status by NotificationsTable.status

}