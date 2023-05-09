package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.MessageForUserTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class MessageForUserEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<MessageForUserEntity>(MessageForUserTable)

    var message: MessageEntity by MessageEntity referencedOn MessageForUserTable.messageId
    var userId: UserEntity by UserEntity referencedOn MessageForUserTable.userId
    var received: Boolean by MessageForUserTable.received
    var read: Boolean by MessageForUserTable.read
    var answer: String? by MessageForUserTable.answer
    var fromGroup: Boolean by MessageForUserTable.fromGroup
}