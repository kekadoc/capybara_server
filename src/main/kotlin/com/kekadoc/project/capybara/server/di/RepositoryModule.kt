package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.repository.api_key.ApiKeysRepository
import com.kekadoc.project.capybara.server.data.repository.api_key.ApiKeysRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.contacts.PublicContactsRepository
import com.kekadoc.project.capybara.server.data.repository.contacts.PublicContactsRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.notification.NotificationRepository
import com.kekadoc.project.capybara.server.data.repository.notification.NotificationRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<ApiKeysRepository> {
        ApiKeysRepositoryImpl(
            apiKeyDataSource = get(),
        )
    }
    single<UsersRepository> {
        UsersRepositoryImpl(
            usersDataSource = get(),
        )
    }
    single<NotificationRepository> {
        NotificationRepositoryImpl(
            notificationsDataSource = get(),
        )
    }
    single<PublicContactsRepository> {
        PublicContactsRepositoryImpl(
            contactsDataSource = get(),
        )
    }
    single<GroupsRepository> {
        GroupsRepositoryImpl(
            dataSource = get(),
        )
    }
}