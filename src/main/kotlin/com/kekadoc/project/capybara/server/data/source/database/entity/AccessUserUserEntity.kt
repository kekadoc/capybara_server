package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.AccessUserUsersTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class AccessUserUserEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<AccessUserUserEntity>(AccessUserUsersTable)

    var fromUser by UserEntity referencedOn AccessUserUsersTable.fromUser
    var toUser by UserEntity referencedOn AccessUserUsersTable.toUser

}