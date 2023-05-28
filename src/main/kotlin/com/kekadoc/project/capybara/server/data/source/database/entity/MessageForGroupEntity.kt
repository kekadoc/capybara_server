package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.MessageForGroupTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class MessageForGroupEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<MessageForGroupEntity>(MessageForGroupTable)

    var message by MessageEntity referencedOn MessageForGroupTable.messageId
    var group by GroupEntity referencedOn MessageForGroupTable.groupId

}