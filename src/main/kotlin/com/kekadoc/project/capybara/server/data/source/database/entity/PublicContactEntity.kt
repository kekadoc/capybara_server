package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.PublicContactsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PublicContactEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<PublicContactEntity>(PublicContactsTable)

    var user by UserEntity referencedOn PublicContactsTable.user

}