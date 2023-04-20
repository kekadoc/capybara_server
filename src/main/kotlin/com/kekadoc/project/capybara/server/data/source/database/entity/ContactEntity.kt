package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.ContactsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class ContactEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<ContactEntity>(ContactsTable)

    var user by UserEntity referencedOn ContactsTable.user
    var communications by ContactsTable.communications

}