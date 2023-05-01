package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.source.api.api_key.ApiKeyDataSource
import com.kekadoc.project.capybara.server.data.source.api.api_key.ApiKeyDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.auth.AuthorizationDataSource
import com.kekadoc.project.capybara.server.data.source.api.auth.AuthorizationDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.contacts.PublicContactsDataSource
import com.kekadoc.project.capybara.server.data.source.api.contacts.PublicContactsDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.group.GroupDataSource
import com.kekadoc.project.capybara.server.data.source.api.group.GroupDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.notification.NotificationsDataSource
import com.kekadoc.project.capybara.server.data.source.api.notification.NotificationsDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.notification.mobile.local.MobileNotificationsLocalDataSource
import com.kekadoc.project.capybara.server.data.source.api.notification.mobile.local.MobileNotificationsLocalDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.notification.mobile.remote.MobileNotificationsRemoteDataSource
import com.kekadoc.project.capybara.server.data.source.api.notification.mobile.remote.MobileNotificationsRemoteDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.user.UsersDataSource
import com.kekadoc.project.capybara.server.data.source.api.user.UsersDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.user.access.UserAccessDataSource
import com.kekadoc.project.capybara.server.data.source.api.user.access.UserAccessDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.user.communication.UserCommunicationsDataSource
import com.kekadoc.project.capybara.server.data.source.api.user.communication.UserCommunicationsDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {
    single<ApiKeyDataSource> { ApiKeyDataSourceImpl(database = get()) }
    single<AuthorizationDataSource> { AuthorizationDataSourceImpl(config = get()) }
    single<PublicContactsDataSource> { PublicContactsDataSourceImpl() }
    single<GroupDataSource> { GroupDataSourceImpl() }
    single<NotificationsDataSource> { NotificationsDataSourceImpl() }
    single<MobileNotificationsLocalDataSource> { MobileNotificationsLocalDataSourceImpl() }
    single<MobileNotificationsRemoteDataSource> { MobileNotificationsRemoteDataSourceImpl(get()) }
    single<UsersDataSource> { UsersDataSourceImpl() }
    single<UserAccessDataSource> { UserAccessDataSourceImpl() }
    single<UserCommunicationsDataSource> { UserCommunicationsDataSourceImpl() }
}