package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.repository.api_key.ApiKeysRepository
import com.kekadoc.project.capybara.server.data.repository.api_key.ApiKeysRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.auth.AuthorizationRepository
import com.kekadoc.project.capybara.server.data.repository.auth.AuthorizationRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.contacts.PublicContactsRepository
import com.kekadoc.project.capybara.server.data.repository.contacts.PublicContactsRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.notification.email.EmailNotificationRepository
import com.kekadoc.project.capybara.server.data.repository.notification.email.EmailNotificationRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.notification.mobile.MobileNotificationsRepository
import com.kekadoc.project.capybara.server.data.repository.notification.mobile.MobileNotificationsRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<ApiKeysRepository> {
        ApiKeysRepositoryImpl(
            apiKeyDataSource = get(),
        )
    }

    single<AuthorizationRepository> {
        AuthorizationRepositoryImpl(
            authorizationDataSource = get(),
            registrationDataSource = get(),
        )
    }

    single<UsersRepository> {
        UsersRepositoryImpl(
            usersDataSource = get(),
            userAccessDataSource = get(),
            userCommunicationsDataSource = get(),
        )
    }

    single<MessagesRepository> {
        MessagesRepositoryImpl(
            messagesDataSource = get(),
        )
    }

    single<MobileNotificationsRepository> {
        MobileNotificationsRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get(),
        )
    }

    single<PublicContactsRepository> {
        PublicContactsRepositoryImpl(
            publicContactsDataSource = get(),
        )
    }

    single<GroupsRepository> {
        GroupsRepositoryImpl(
            dataSource = get(),
        )
    }

    single<EmailNotificationRepository> {
        EmailNotificationRepositoryImpl(
            emailNotificationDataSource = get(),
        )
    }

}