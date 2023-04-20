package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

//
object AccessUserContactsTable : UUIDTable("access_user_contacts") {

    val user = reference("user", UsersTable)
    val contact = reference("contact", ContactsTable)

}