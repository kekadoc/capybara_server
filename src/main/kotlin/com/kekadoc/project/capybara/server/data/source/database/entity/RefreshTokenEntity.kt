package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.RefreshTokensTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class RefreshTokenEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<RefreshTokenEntity>(RefreshTokensTable)

    var token by RefreshTokensTable.token
    var expiresAt by RefreshTokensTable.expiresAt
    var userId by RefreshTokensTable.userId

}