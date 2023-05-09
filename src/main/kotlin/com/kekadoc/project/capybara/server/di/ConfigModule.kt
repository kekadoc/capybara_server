package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.Application
import com.kekadoc.project.capybara.server.data.source.api.notifications.email.EmailNotificationConfig
import com.kekadoc.project.capybara.server.data.source.database.DatabaseConfig
import org.koin.dsl.module

val configModule = module {

    single<DatabaseConfig> {
        if (Application.isDebug) {
            DatabaseConfig(
                hostName = "",
                port = 5432,
                database = "postgres",
                username = "postgres",
                password = "1",
                internalUrl = "jdbc:postgresql://localhost:5432/",
                externalUrl = "jdbc:postgresql://localhost:5432/",
            )
        } else {
            DatabaseConfig(
                hostName = "dpg-chd8lmd269vdj6ap32v0-a",
                port = 5432,
                database = "capybara_db",
                username = "admin",
                password = "WDwsR7nlluQooq4omvpjI6jgazaSpDgW",
                internalUrl = "postgres://admin:WDwsR7nlluQooq4omvpjI6jgazaSpDgW@dpg-chd8lmd269vdj6ap32v0-a/capybara_db",
                externalUrl = "postgres://admin:WDwsR7nlluQooq4omvpjI6jgazaSpDgW@dpg-chd8lmd269vdj6ap32v0-a.singapore-postgres.render.com/capybara_db",
            )
        }
    }

    single<EmailNotificationConfig> {
        if (Application.isDebug) {
            EmailNotificationConfig(
                tokenSecret = "213_email_secret_acdef",
                hostName = "smtp.yandex.ru",
                smtpPort = 465,
                username = "ya.zer567@ya.ru",
                password = "&3igVB.?/Zx3HNj",
                fromEmail = "ya.zer567@ya.ru",
                subject = "Чайковский филиал ПНИПУ",
            )
        } else {
            // TODO: Production config
            EmailNotificationConfig(
                tokenSecret = "213_email_secret_acdef",
                hostName = "smtp.yandex.ru",
                smtpPort = 465,
                username = "ya.zer567@ya.ru",
                password = "&3igVB.?/Zx3HNj",
                fromEmail = "ya.zer567@ya.ru",
                subject = "Чайковский филиал ПНИПУ",
            )
        }
    }

}