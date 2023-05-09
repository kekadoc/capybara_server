package com.kekadoc.project.capybara.server.routing.api.notifications

import com.kekadoc.project.capybara.server.routing.api.notifications.email.emailNotifications
import com.kekadoc.project.capybara.server.routing.api.notifications.mobile.mobileNotifications
import io.ktor.server.routing.*

fun Route.notifications() = route("/notifications") {

    mobileNotifications()

    emailNotifications()

}
