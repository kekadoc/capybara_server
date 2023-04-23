package com.kekadoc.project.capybara.server

import com.kekadoc.project.capybara.server.common.component.Component
import com.kekadoc.project.capybara.server.data.source.database.Database
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.firebase.Firebase

object Application : Component {

    private val components: List<Component> = listOf(
        Di,
        Database,
        Firebase,
        Server,
    )

    override fun init() { components.forEach(Component::init) }

}