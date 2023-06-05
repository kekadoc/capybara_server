package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.common.component.Component
import io.ktor.server.application.*
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.scope.Scope

object Di : KoinComponent, Component {
    override fun init(application: Application) {
        startKoin {
            application(application)
            modules(
                configModule,
                serverModule,
                firebaseModule,
                interactorsModule,
                interactorFunctionsModule,
                managersModule,
                repositoryModule,
                dataSourceModule,
                dataServicesModule,
                scheduleModule,
                dataFunctionsModule,
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