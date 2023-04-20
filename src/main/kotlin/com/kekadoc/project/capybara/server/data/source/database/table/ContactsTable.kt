package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

//
object ContactsTable : UUIDTable("contacts") {

    val user = reference("user", UsersTable)
    val communications = text("communications")

}