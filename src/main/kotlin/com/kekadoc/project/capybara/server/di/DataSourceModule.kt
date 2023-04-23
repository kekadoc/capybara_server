package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.source.api.api_key.ApiKeyDataSource
import com.kekadoc.project.capybara.server.data.source.api.api_key.ApiKeyDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.contacts.ContactsDataSource
import com.kekadoc.project.capybara.server.data.source.api.contacts.FDContactsDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.group.FDGroupDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.group.GroupDataSource
import com.kekadoc.project.capybara.server.data.source.api.notification.FDNotificationsDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.notification.NotificationsDataSource
import com.kekadoc.project.capybara.server.data.source.api.notification.mobile.FMMobileNotificationsDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.notification.mobile.MobileNotificationsDataSource
import com.kekadoc.project.capybara.server.data.source.api.user.FDUsersDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.user.UsersDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single<ApiKeyDataSource> { ApiKeyDataSourceImpl(database = get()) }
    single<ContactsDataSource> { FDContactsDataSourceImpl(database = get()) }
    single<GroupDataSource> { FDGroupDataSourceImpl(database = get()) }
    single<NotificationsDataSource> { FDNotificationsDataSourceImpl(database = get()) }
    single<MobileNotificationsDataSource> { FMMobileNotificationsDataSourceImpl(firebaseMessaging = get()) }
    single<UsersDataSource> { FDUsersDataSourceImpl(database = get()) }
}