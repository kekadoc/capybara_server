package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.GroupsTable
import com.kekadoc.project.capybara.server.data.source.database.table.UsersGroupsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class GroupEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<GroupEntity>(GroupsTable)

    var name by GroupsTable.name

    val members by UserGroupEntity referrersOn UsersGroupsTable.group

}