package com.kekadoc.project.capybara.server.routing.verifier

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.http.Header
import com.kekadoc.project.capybara.server.routing.getApiKeyFromQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

interface Verifier {
    
    suspend fun PipelineContext.verify()
    
    companion object : Verifier {
        override suspend fun PipelineContext.verify() {
            TODO("Not yet implemented")
        }
    }
}

class Verifiers(
    private val verifiers: Set<Verifier>
) : Verifier {
    override suspend fun PipelineContext.verify() {
        verifiers.forEach {
            it.apply { verify() }
        }
    }
}

private class ApiKeyVerifier() : Verifier {
    override suspend fun PipelineContext.verify() {
        val apiKey = getApiKeyFromQuery()
        call.request.header(Header.Authorization.name)  // TODO: 16.08.2022
        if (apiKey.isEmpty()) {
            throw HttpException(
                statusCode = HttpStatusCode.Forbidden,
                message = "ApiKey not found"
            )
        }
    }
}

suspend inline fun PipelineContext.verify(verifier: Verifier, block: PipelineContext.() -> Unit) {
    verifier.apply { verify() }
    block()
}