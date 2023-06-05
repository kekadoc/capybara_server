package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.service.email.EmailDataService
import com.kekadoc.project.capybara.server.data.service.email.EmailDataServiceImpl
import org.koin.dsl.module

val dataServicesModule = module {

    single<EmailDataService> {
        EmailDataServiceImpl(
            config = get(),
        )
    }

}