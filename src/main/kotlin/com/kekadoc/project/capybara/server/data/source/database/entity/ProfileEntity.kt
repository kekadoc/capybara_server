package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.ProfilesTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class ProfileEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<ProfileEntity>(ProfilesTable)

    var type by ProfilesTable.type
    var name by ProfilesTable.name
    var surname by ProfilesTable.surname
    var patronymic by ProfilesTable.patronymic
    var avatar by ProfilesTable.avatar
    var about by ProfilesTable.about

}