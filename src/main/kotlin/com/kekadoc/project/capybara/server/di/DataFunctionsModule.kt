package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.data.function.create_user.CreateUserFunction
import com.kekadoc.project.capybara.server.data.function.create_user.CreateUserFunctionImpl
import org.koin.dsl.module

val dataFunctionsModule = module {

    single<CreateUserFunction> {
        CreateUserFunctionImpl(
            usersRepository = get(),
        )
    }

}