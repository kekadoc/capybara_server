package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.manager.message_with_notification.MessageWithNotificationManager
import com.kekadoc.project.capybara.server.data.manager.message_with_notification.MessageWithNotificationManagerImpl
import com.kekadoc.project.capybara.server.data.manager.registration.RegistrationManager
import com.kekadoc.project.capybara.server.data.manager.registration.RegistrationManagerImpl
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

    single<RegistrationManager> {
        RegistrationManagerImpl(
            authorizationRepository = get(),
            usersRepository = get(),
            groupsRepository = get(),
            emailDataService = get(),
            createUserFunction = get(),
        )
    }

}