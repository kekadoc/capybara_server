package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.*
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<UserEntity>(UsersTable)

    var login by UsersTable.login
    var password by UsersTable.password
    var profile by ProfileEntity referencedOn UsersTable.profile

    val communications by CommunicationEntity referrersOn CommunicationsTable.user
    val groups by UserGroupEntity referrersOn UsersGroupsTable.user

}