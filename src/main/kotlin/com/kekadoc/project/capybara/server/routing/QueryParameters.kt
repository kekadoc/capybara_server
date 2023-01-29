package com.kekadoc.project.capybara.server.routing

import com.kekadoc.project.capybara.server.Config
import com.kekadoc.project.capybara.server.common.PipelineContext
import io.ktor.server.application.*

internal fun PipelineContext.getApiKeyFromQuery(): String {
    return call.request.queryParameters[Config.API_KEY_PARAMETER].orEmpty()
}