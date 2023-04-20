package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.AccessUserGroupsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class AccessUserGroupEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<AccessUserGroupEntity>(AccessUserGroupsTable)

    var user by UserEntity referencedOn AccessUserGroupsTable.user
    var group by GroupEntity referencedOn AccessUserGroupsTable.group

}