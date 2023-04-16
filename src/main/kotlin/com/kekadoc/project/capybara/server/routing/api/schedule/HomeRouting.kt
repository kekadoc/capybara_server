package com.kekadoc.project.capybara.server.routing.api.schedule

import com.kekadoc.project.capybara.server.Config
import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

internal suspend fun PipelineContext.homeResponse() = execute(ApiKeyVerifier) {
    val message = "PNIPU schedule parser by Oleg\nv:${Config.VERSION}"
    call.respondText(message, ContentType.Text.Plain)
}