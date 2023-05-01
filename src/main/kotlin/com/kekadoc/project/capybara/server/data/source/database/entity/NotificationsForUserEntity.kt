package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.NotificationsForUsersTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class NotificationsForUserEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<NotificationsForUserEntity>(NotificationsForUsersTable)

    var notificationId: NotificationEntity by NotificationEntity referencedOn NotificationsForUsersTable.notificationId
    var userId: UserEntity by UserEntity referencedOn NotificationsForUsersTable.userId
    var received: Boolean by NotificationsForUsersTable.received
    var read: Boolean by NotificationsForUsersTable.read
    var answer: String? by NotificationsForUsersTable.answer
    var fromGroup: Boolean by NotificationsForUsersTable.fromGroup
}