package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.manager.message_with_notification.MessageWithNotificationManager
import com.kekadoc.project.capybara.server.data.manager.message_with_notification.MessageWithNotificationManagerImpl
import org.koin.dsl.module

val managersModule = module {

    single<MessageWithNotificationManager> {
        MessageWithNotificationManagerImpl(
            usersRepository = get(),
            messagesRepository = get(),
            emailNotificationRepository = get(),
            mobileNotificationsRepository = get(),
            groupsRepository = get(),
        )
    }

}