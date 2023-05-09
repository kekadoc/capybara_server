package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.MessageForGroupTable
import com.kekadoc.project.capybara.server.data.source.database.table.MessageForUserTable
import com.kekadoc.project.capybara.server.data.source.database.table.MessageTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class MessageEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<MessageEntity>(MessageTable)

    var author: UserEntity by UserEntity referencedOn MessageTable.author
    var type: String by MessageTable.type
    var contentTitle: String? by MessageTable.contentTitle
    var contentText: String by MessageTable.contentText
    var contentImage: String? by MessageTable.contentImage
    var status: String by MessageTable.status
    var action1: String? by MessageTable.action1
    var action2: String? by MessageTable.action2
    var action3: String? by MessageTable.action3
    var notificationEmail: Boolean by MessageTable.notificationEmail
    var notificationSms: Boolean by MessageTable.notificationSms
    var notificationApp: Boolean by MessageTable.notificationApp
    var notificationMessengers: Boolean by MessageTable.notificationMessengers

    val addresseeUsers by MessageForUserEntity referrersOn MessageForUserTable.messageId
    val addresseeGroups by MessageForGroupEntity referrersOn MessageForGroupTable.message_id

}