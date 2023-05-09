package com.kekadoc.project.capybara.server.data.source.database

import com.kekadoc.project.capybara.server.common.component.Component
import com.kekadoc.project.capybara.server.data.source.database.table.*
import com.kekadoc.project.capybara.server.di.Di
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.get
import org.jetbrains.exposed.sql.DatabaseConfig.Companion as ExposedDatabaseConfig

object Database : Component {

    override fun init(application: Application) {
        val config = Di.get<DatabaseConfig>()
        val db = Database.connect(
            url = config.internalUrl,
            driver = "org.postgresql.Driver",
            user = config.username,
            password = config.password,
            databaseConfig = ExposedDatabaseConfig {
                //Configurations
            }
        )
        Di.getKoin().declare(db)
        //org.jetbrains.exposed.dao.EntityHook.subscribe {  }
        transaction {
            SchemaUtils.create(
                CommunicationsTable,
                GroupsTable,
                MobilePushNotificationsTable,
                MessageForGroupTable,
                MessageForUserTable,
                MessageTable,
                ProfilesTable,
                PublicContactsTable,
                PushTokensTable,
                UserAccessToGroupTable,
                UserAccessToUserTable,
                UsersGroupsTable,
                UsersTable,
            )
        }
    }

}