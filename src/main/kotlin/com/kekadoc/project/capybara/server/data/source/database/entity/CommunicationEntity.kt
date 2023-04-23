package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.CommunicationsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class CommunicationEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<CommunicationEntity>(CommunicationsTable)

    var user by UserEntity referencedOn CommunicationsTable.user
    var type by CommunicationsTable.type
    var value by CommunicationsTable.value

}