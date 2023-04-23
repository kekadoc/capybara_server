package com.kekadoc.project.capybara.server.data.source.database

import com.kekadoc.project.capybara.server.common.component.Component
import com.kekadoc.project.capybara.server.data.source.database.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object Database : Component {

    override fun init() {
        val db = Database.connect(
            "jdbc:postgresql://localhost:5432/",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "1"
        )
        transaction {
            SchemaUtils.create(
                UsersTable,
                ProfilesTable,
                UserCharactersTable,
                AuthTokensTable,
                PushTokensTable,
                GroupsTable,
                UsersGroupsTable,
                ContactsTable,
                AccessUserUsersTable,
                AccessUserGroupsTable,
                AccessUserContactsTable,
                NotificationsTable,
                NotificationsForUsersTable,
                NotificationsForGroupsTable,
            )
        }
    }

}