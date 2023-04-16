package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.repository.api_key.ApiKeysRepository
import com.kekadoc.project.capybara.server.data.repository.api_key.ApiKeysRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.contacts.ContactsRepository
import com.kekadoc.project.capybara.server.data.repository.contacts.ContactsRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.group.GroupRepository
import com.kekadoc.project.capybara.server.data.repository.group.GroupRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepositoryImpl
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
    single<MessagesRepository> {
        MessagesRepositoryImpl(
            messagesDataSource = get(),
            notificationsDataSource = get(),
        )
    }
    single<ContactsRepository> {
        ContactsRepositoryImpl(
            contactsDataSource = get(),
        )
    }
    single<GroupRepository> {
        GroupRepositoryImpl(
            dataSource = get(),
        )
    }
}