package com.kekadoc.project.capybara.server.routing

import com.kekadoc.project.capybara.server.common.PipelineContext
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

internal suspend fun PipelineContext.homePNIPUResponse() = execute {
    call.respondText("Расписание Чайквоского филлиала ПНИПУ", ContentType.Text.Plain)
}