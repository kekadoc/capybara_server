package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.common.component.Component
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

object Di : KoinComponent, Component {
    override fun init() {
        startKoin {
            modules(
                firebaseModule,
                interactorsModule,
                repositoryModule,
                dataSourceModule,
                scheduleModule,
                jsonModule,
            )
        }
    }
}