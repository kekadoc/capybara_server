package com.kekadoc.project.capybara.server.common.component

import io.ktor.server.application.*

interface Component {

    suspend fun init(application: Application)

}