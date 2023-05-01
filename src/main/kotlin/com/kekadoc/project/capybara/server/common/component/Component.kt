package com.kekadoc.project.capybara.server.common.component

import io.ktor.server.application.*

interface Component {

    fun init(application: Application)

}