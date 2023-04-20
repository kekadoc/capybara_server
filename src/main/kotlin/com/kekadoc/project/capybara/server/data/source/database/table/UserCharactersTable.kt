package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

//
object UserCharactersTable : UUIDTable("user_characters") {

    val salt_0 = varchar("salt_0", 255)
    val salt_1 = varchar("salt_1", 255)
    val salt_2 = varchar("salt_2", 255)
    val salt_3 = varchar("salt_3", 255)
    val salt_4 = varchar("salt_4", 255)

}