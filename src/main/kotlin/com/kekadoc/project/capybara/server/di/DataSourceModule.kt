package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.source.api.api_key.ApiKeyDataSource
import com.kekadoc.project.capybara.server.data.source.api.api_key.ApiKeyDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.auth.AuthorizationDataSource
import com.kekadoc.project.capybara.server.data.source.api.auth.AuthorizationDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.auth.RegistrationDataSource
import com.kekadoc.project.capybara.server.data.source.api.auth.RegistrationDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.contacts.PublicContactsDataSource
import com.kekadoc.project.capybara.server.data.source.api.contacts.PublicContactsDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.group.GroupDataSource
import com.kekadoc.project.capybara.server.data.source.api.group.GroupDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.messages.MessagesDataSource
import com.kekadoc.project.capybara.server.data.source.api.messages.MessagesDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.notifications.email.EmailNotificationDataSource
import com.kekadoc.project.capybara.server.data.source.api.notifications.email.EmailNotificationDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.local.MobileNotificationsLocalDataSource
import com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.local.MobileNotificationsLocalDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.remote.MobileNotificationsRemoteDataSource
import com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.remote.MobileNotificationsRemoteDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.user.UsersDataSource
import com.kekadoc.project.capybara.server.data.source.api.user.UsersDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.user.access.UserAccessDataSource
import com.kekadoc.project.capybara.server.data.source.api.user.access.UserAccessDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.api.user.communication.UserCommunicationsDataSource
import com.kekadoc.project.capybara.server.data.source.api.user.communication.UserCommunicationsDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.local.LocalDataSource
import com.kekadoc.project.capybara.server.data.source.local.LocalDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {

    single<ApiKeyDataSource> {
        ApiKeyDataSourceImpl(
            database = get(),
        )
    }

    single<LocalDataSource> {
        LocalDataSourceImpl()
    }

    single<AuthorizationDataSource> {
        AuthorizationDataSourceImpl(
            config = get(),
        )
    }

    single<RegistrationDataSource> {
        RegistrationDataSourceImpl()
    }

    single<PublicContactsDataSource> { PublicContactsDataSourceImpl() }

    single<GroupDataSource> { GroupDataSourceImpl() }

    single<MessagesDataSource> { MessagesDataSourceImpl() }

    single<MobileNotificationsLocalDataSource> { MobileNotificationsLocalDataSourceImpl() }

    single<MobileNotificationsRemoteDataSource> { MobileNotificationsRemoteDataSourceImpl(get()) }

    single<UsersDataSource> { UsersDataSourceImpl() }

    single<UserAccessDataSource> { UserAccessDataSourceImpl() }

    single<UserCommunicationsDataSource> { UserCommunicationsDataSourceImpl() }

    single<EmailNotificationDataSource> {
        EmailNotificationDataSourceImpl(
            config = get(),
        )
    }

}