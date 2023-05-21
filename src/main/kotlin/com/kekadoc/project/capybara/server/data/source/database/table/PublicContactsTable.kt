package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object PublicContactsTable : UUIDTable("public_contacts") {

    val user = reference(
        name = "user",
        foreign = UsersTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )

}