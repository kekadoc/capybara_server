package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.intercator.functions.FetchUserByAccessTokenFunction
import org.koin.dsl.module

val interactorFunctionsModule = module {

    single {
        FetchUserByAccessTokenFunction(
            authorizationRepository = get(),
            userRepository = get(),
        )
    }

}