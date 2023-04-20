package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.PushTokensTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PushTokenEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<PushTokenEntity>(PushTokensTable)

    var user by UserEntity referencedOn PushTokensTable.user
    var token by PushTokensTable.token

}