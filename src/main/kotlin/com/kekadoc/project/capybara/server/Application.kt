package com.kekadoc.project.capybara.server

import com.kekadoc.project.capybara.server.common.component.Component
import com.kekadoc.project.capybara.server.data.source.database.Database
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.firebase.Firebase
import com.kekadoc.project.capybara.server.test.Test
import io.ktor.server.application.Application

object Application : Component {

    private val components: List<Component> = listOf(
        Di,
        Database,
        Firebase,
        Test,
        Server,
    )

    override fun init(application: Application) { components.forEach { it.init(application) }}

}