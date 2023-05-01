package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object PublicContactsTable : UUIDTable("public_contacts") {

    val user = reference("user", UsersTable)

}