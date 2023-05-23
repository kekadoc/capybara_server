package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.intercator.functions.GetReceivedMessageFunction
import org.koin.dsl.module

val interactorFunctionsModule = module {

    single {
        FetchUserByAccessTokenFunction(
            authorizationRepository = get(),
            userRepository = get(),
        )
    }

    single {
        GetReceivedMessageFunction(
            messagesRepository = get(),
            usersRepository = get(),
        )
    }

}