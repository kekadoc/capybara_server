package com.kekadoc.project.capybara.server

import com.kekadoc.project.capybara.server.common.component.Component
import com.kekadoc.project.capybara.server.data.source.database.Database
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.services.firebase.Firebase
import com.kekadoc.project.capybara.server.utils.test.Test
import io.ktor.server.application.Application

object Application : Component {

    const val isDebug = true

    private val components: List<Component> = listOf(
        Di,
        Database,
        Firebase,
        Test,
        Server,
    )

    override fun init(application: Application) {
        components.forEach { it.init(application) }
    }

}