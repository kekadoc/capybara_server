package com.kekadoc.project.capybara.server.routing.api.schedule

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.routing.util.execute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

internal suspend fun PipelineContext.homePNIPUResponse() = execute {
    call.respondText("Расписание Чайквоского филлиала ПНИПУ", ContentType.Text.Plain)
}