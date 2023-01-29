package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.parser.api.ScheduleApi
import org.koin.dsl.module

val scheduleModule = module {
    single {
        ScheduleApi.getDefaultInstance()
    }
}