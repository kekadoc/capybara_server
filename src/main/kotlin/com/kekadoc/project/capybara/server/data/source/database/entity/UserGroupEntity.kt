package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.UsersGroupsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserGroupEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<UserGroupEntity>(UsersGroupsTable)

    var user by UserEntity referencedOn UsersGroupsTable.user
    var group by GroupEntity referencedOn UsersGroupsTable.group

}