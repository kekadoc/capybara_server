package com.kekadoc.project.capybara.server.routing.verifier

import com.kekadoc.project.capybara.server.Config
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.repository.api_key.ApiKeysRepository
import com.kekadoc.project.capybara.server.di.Di
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.coroutines.flow.first
import org.koin.core.component.get

object ApiKeyVerifier : Verifier {

    override suspend fun verify(call: ApplicationCall) {
        val apiKey = call.getApiKeyFromQuery() ?: call.getApiKeyFromHeader()
        println("apiKey=$apiKey")
        if (apiKey.isNullOrEmpty()) {
            throw HttpException(
                statusCode = HttpStatusCode.Forbidden,
                message = "ApiKey not found"
            )
        }
        val isApiKeyValid = Di.get<ApiKeysRepository>().isApiKeyValid(apiKey).first()
        if (!isApiKeyValid) {
            throw HttpException(
                statusCode = HttpStatusCode.Forbidden,
                message = "ApiKey not valid",
            )
        }
    }

    private fun ApplicationCall.getApiKeyFromQuery(): String? {
        return request.queryParameters[Config.API_KEY_QUERY]
    }

    private fun ApplicationCall.getApiKeyFromHeader(): String? {
        return request.header(Config.API_KEY_HEADER)
    }


}