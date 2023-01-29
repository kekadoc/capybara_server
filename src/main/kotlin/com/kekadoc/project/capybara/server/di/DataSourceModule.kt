package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.source.api_key.ApiKeyDataSource
import com.kekadoc.project.capybara.server.data.source.api_key.ApiKeyDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.group.GroupDataSource
import com.kekadoc.project.capybara.server.data.source.group.GroupDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.message.MessageDataSource
import com.kekadoc.project.capybara.server.data.source.message.MessageDataSourceImpl
import com.kekadoc.project.capybara.server.data.source.user.UserDataSource
import com.kekadoc.project.capybara.server.data.source.user.UserDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {
    single<UserDataSource> { UserDataSourceImpl() }
    single<ApiKeyDataSource> { ApiKeyDataSourceImpl(database = get()) }
    single<GroupDataSource> { GroupDataSourceImpl() }
    single<MessageDataSource> { MessageDataSourceImpl() }
}