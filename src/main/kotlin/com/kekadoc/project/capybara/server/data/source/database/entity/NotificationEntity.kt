package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.NotificationsForGroupsTable
import com.kekadoc.project.capybara.server.data.source.database.table.NotificationsForUsersTable
import com.kekadoc.project.capybara.server.data.source.database.table.NotificationsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class NotificationEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<NotificationEntity>(NotificationsTable)

    var author: UserEntity by UserEntity referencedOn NotificationsTable.author
    var type: String by NotificationsTable.type
    var contentTitle: String? by NotificationsTable.contentTitle
    var contentText: String by NotificationsTable.contentText
    var contentImage: String? by NotificationsTable.contentImage
    var status: String by NotificationsTable.status

    val addresseeUsers by NotificationsForUserEntity referrersOn NotificationsForUsersTable.notificationId
    val addresseeGroups by NotificationsForGroupEntity referrersOn NotificationsForGroupsTable.notificationId

}