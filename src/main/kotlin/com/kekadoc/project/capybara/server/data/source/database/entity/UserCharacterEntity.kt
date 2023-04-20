package com.kekadoc.project.capybara.server.data.source.database.entity

import com.kekadoc.project.capybara.server.data.source.database.table.UserCharactersTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserCharacterEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<UserCharacterEntity>(UserCharactersTable)

    var salt_0 by UserCharactersTable.salt_0
    var salt_1 by UserCharactersTable.salt_1
    var salt_2 by UserCharactersTable.salt_2
    var salt_3 by UserCharactersTable.salt_3
    var salt_4 by UserCharactersTable.salt_4

}