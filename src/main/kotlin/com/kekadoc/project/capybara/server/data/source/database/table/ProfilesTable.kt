package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

//
object ProfilesTable : UUIDTable("profiles") {

    val type = varchar("type", 255)
    val name = text("name")
    val surname = text("surname")
    val patronymic = text("patronymic")
    val avatar = text("avatar")
    val role = text("role")
    val about = text("about")

}