package com.kekadoc.project.capybara.server.routing

import com.kekadoc.project.capybara.server.Config
import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.extensions.emptyString
import com.kekadoc.project.capybara.server.data.repository.api_key.ApiKeyRepository
import com.kekadoc.project.capybara.server.di.Di
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.flow.first
import org.koin.core.component.get

internal suspend fun PipelineContext.homeResponse() = execute {
    val apiKeyRepository = Di.get<ApiKeyRepository>()
    val baseMessage = "PNIPU schedule parser by Oleg\nv:${Config.VERSION}"
    val apiKey = getApiKeyFromQuery()
    val isApiKeyValid = apiKeyRepository.isApiKeyValid(apiKey).first()
    val message = baseMessage + if (!isApiKeyValid) {
        "\nYou need an apiKey to use the service"
    } else {
        emptyString()
    }
    call.respondText(message, ContentType.Text.Plain)
}