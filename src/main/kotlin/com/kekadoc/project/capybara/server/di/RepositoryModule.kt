package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.repository.api_key.ApiKeyRepository
import com.kekadoc.project.capybara.server.data.repository.api_key.ApiKeyRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.auth.AuthRepository
import com.kekadoc.project.capybara.server.data.repository.auth.AuthRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.message.MessageRepository
import com.kekadoc.project.capybara.server.data.repository.message.MessageRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.notification.NotificationRepository
import com.kekadoc.project.capybara.server.data.repository.notification.NotificationRepositoryImpl
import com.kekadoc.project.capybara.server.data.repository.user.UserRepository
import com.kekadoc.project.capybara.server.data.repository.user.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(userDataSource = get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(userDataSource = get()) }
    single<UserRepository> { UserRepositoryImpl(userDataSource = get()) }
    single<ApiKeyRepository> { ApiKeyRepositoryImpl(apiKeyDataSource = get()) }
    single<MessageRepository> { MessageRepositoryImpl(messageDataSource = get()) }
}