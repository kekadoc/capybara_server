package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.common.component.Component
import io.ktor.server.application.*
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.scope.Scope

object Di : KoinComponent, Component {
    override fun init(application: Application) {
        startKoin {
            application(application)
            modules(
                serverModule,
                firebaseModule,
                interactorsModule,
                interactorFunctionsModule,
                repositoryModule,
                dataSourceModule,
                scheduleModule,
                jsonModule,
            )
        }
    }
}

fun KoinApplication.application(application: Application) {
    this.koin.declare(application)
}

fun Scope.ktorApplication(): Application {
    return get<Application>()
}