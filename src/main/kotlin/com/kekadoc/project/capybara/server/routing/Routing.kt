package com.kekadoc.project.capybara.server.routing

import com.kekadoc.project.capybara.server.routing.api.addressees.addressees
import com.kekadoc.project.capybara.server.routing.api.auth.auth
import com.kekadoc.project.capybara.server.routing.api.contacts.contacts
import com.kekadoc.project.capybara.server.routing.api.groups.groups
import com.kekadoc.project.capybara.server.routing.api.messages.messages
import com.kekadoc.project.capybara.server.routing.api.notifications.notifications
import com.kekadoc.project.capybara.server.routing.api.profile.profile
import com.kekadoc.project.capybara.server.routing.api.schedule.schedule
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() = routing {

    schedule()

    route("/api/v1/") {
        auth()
        profile()
        groups()
        messages()
        notifications()
        contacts()
        addressees()
    }


}