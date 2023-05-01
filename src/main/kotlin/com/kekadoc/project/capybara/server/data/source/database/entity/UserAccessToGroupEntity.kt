package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.UserAccessToGroupTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserAccessToGroupEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<UserAccessToGroupEntity>(UserAccessToGroupTable)

    var user by UserEntity referencedOn UserAccessToGroupTable.user
    var group by GroupEntity referencedOn UserAccessToGroupTable.group

    var readInfo by UserAccessToGroupTable.readInfo
    var readMembers by UserAccessToGroupTable.readMembers
    var sentNotification by UserAccessToGroupTable.sentNotification

}