package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.source.api_key.ApiKeyDataSource
import com.kekadoc.project.capybara.server.data.source.api_key.ApiKeyDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.contacts.ContactsDataSource
import com.kekadoc.project.capybara.server.data.source.contacts.ContactsDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.group.GroupDataSource
import com.kekadoc.project.capybara.server.data.source.group.GroupDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.message.MessageDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.message.MessagesDataSource
import com.kekadoc.project.capybara.server.data.source.notification.NotificationsDataSource
import com.kekadoc.project.capybara.server.data.source.notification.NotificationsDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.user.UsersDataSource
import com.kekadoc.project.capybara.server.data.source.user.UsersDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {
    single<ApiKeyDataSource> { ApiKeyDataSourceImpl(database = get()) }
    single<ContactsDataSource> { ContactsDataSourceImpl(database = get()) }
    single<GroupDataSource> { GroupDataSourceImpl(database = get()) }
    single<MessagesDataSource> { MessageDataSourceImpl(database = get()) }
    single<NotificationsDataSource> { NotificationsDataSourceImpl(firebaseMessaging = get()) }
    single<UsersDataSource> { UsersDataSourceImpl(database = get()) }
}