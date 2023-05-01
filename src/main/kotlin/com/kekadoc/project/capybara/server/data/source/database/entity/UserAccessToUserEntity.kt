package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.UserAccessToUserTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserAccessToUserEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<UserAccessToUserEntity>(UserAccessToUserTable)

    var fromUser by UserEntity referencedOn UserAccessToUserTable.fromUser
    var toUser by UserEntity referencedOn UserAccessToUserTable.toUser

    var readProfile by UserAccessToUserTable.readProfile
    var sentNotification by UserAccessToUserTable.sentNotification
    var contactInfo by UserAccessToUserTable.contactInfo

}