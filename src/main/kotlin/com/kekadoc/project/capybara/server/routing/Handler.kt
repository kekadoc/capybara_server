package com.kekadoc.project.capybara.server.routing

import com.kekadoc.project.capybara.model.ScheduleSource
import com.kekadoc.project.capybara.server.Config
import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.exception.ScheduleNotAvailableException
import com.kekadoc.project.capybara.server.common.exception.UnauthorizedException
import com.kekadoc.project.capybara.server.data.repository.api_key.ApiKeyRepository
import com.kekadoc.project.capybara.server.di.Di
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.flow.first
import org.koin.core.component.get

internal fun PipelineContext.checkIsSourceAvailable(source: ScheduleSource) {
    if (!Config.isSourceAvailable(source)) throw ScheduleNotAvailableException()
}

internal suspend fun PipelineContext.verificationApiKey() {
    val apiKey = getApiKeyFromQuery()
    val apiKeyRepository = Di.get<ApiKeyRepository>()
    val isApiKeyValid = apiKeyRepository.isApiKeyValid(apiKey).first()
    if (!isApiKeyValid) throw HttpException(HttpStatusCode.Unauthorized)
}

internal suspend fun PipelineContext.handleError(e: Throwable) {
    when (e) {
        is HttpException -> {
            call.respond(e.statusCode, e.message.orEmpty())
        }
        is UnauthorizedException -> {
            call.respond(HttpStatusCode.Unauthorized, "Invalid ApiKey")
        }
        is ScheduleNotAvailableException -> {
            call.respond(HttpStatusCode.NotFound, "Schedule source not found")
        }
        else -> {
            call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
        }
    }
}

internal suspend fun <T> PipelineContext.execute(block: suspend () -> T) {
    try {
        block()
    } catch (e: Throwable) {
        handleError(e)
    }
}