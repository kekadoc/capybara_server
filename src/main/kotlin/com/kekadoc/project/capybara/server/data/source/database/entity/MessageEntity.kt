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

    var author by UserEntity referencedOn MessageTable.author
    var type by MessageTable.type
    var contentTitle by MessageTable.contentTitle
    var date by MessageTable.date
    var contentText by MessageTable.contentText
    var status by MessageTable.status
    var actions by MessageTable.actions
    var isMultiAnswer by MessageTable.isMultiAnswer
    var notificationEmail by MessageTable.notificationEmail
    var notificationApp by MessageTable.notificationApp
    var notificationMessengers by MessageTable.notificationMessengers

    val addresseeUsers by MessageForUserEntity referrersOn MessageForUserTable.messageId
    val addresseeGroups by MessageForGroupEntity referrersOn MessageForGroupTable.messageId

}