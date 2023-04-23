package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.AccessUserContactsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class AccessUserContactEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<AccessUserContactEntity>(AccessUserContactsTable)

    var user by UserEntity referencedOn AccessUserContactsTable.user
    var contact by UserEntity referencedOn AccessUserContactsTable.contact

}