package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.AuthTokensTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class AuthTokenEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<AuthTokenEntity>(AuthTokensTable)

    var user by UserEntity referencedOn AuthTokensTable.user
    var token by AuthTokensTable.token
    var createdAt by AuthTokensTable.createdAt

}