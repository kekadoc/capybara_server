package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.NotificationsForGroupsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class NotificationsForGroupEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<NotificationsForGroupEntity>(NotificationsForGroupsTable)

    var notificationId by NotificationEntity referencedOn NotificationsForGroupsTable.notificationId
    var group by GroupEntity referencedOn NotificationsForGroupsTable.group

}