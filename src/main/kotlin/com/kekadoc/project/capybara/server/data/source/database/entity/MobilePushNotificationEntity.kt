package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.MobilePushNotificationsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class MobilePushNotificationEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<MobilePushNotificationEntity>(MobilePushNotificationsTable)

    var user by UserEntity referencedOn MobilePushNotificationsTable.user
    var notification by NotificationEntity referencedOn MobilePushNotificationsTable.notification
    var pushId by MobilePushNotificationsTable.pushId

}