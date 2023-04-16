package com.kekadoc.project.capybara.server.routing.verifier

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.http.Header
import com.kekadoc.project.capybara.server.common.http.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.websocket.*

object AuthorizationVerifier : Verifier {

    context(PipelineContext)
    fun requireAuthorizationToken(): String {
        return call.getAuthorizationToken() ?: throw HttpException(HttpStatusCode.Unauthorized)
    }

    context(WebSocketServerSession)
    fun requireAuthorizationToken(): String {
        return call.getAuthorizationToken() ?: throw HttpException(HttpStatusCode.Unauthorized)
    }

    context(ApplicationCall)
    fun requireAuthorizationToken(): String {
        return getAuthorizationToken() ?: throw HttpException(HttpStatusCode.Unauthorized)
    }

    private fun ApplicationCall.getAuthorizationToken(): String? {
        return getAuthorizationHeader()?.value
    }

    private fun ApplicationCall.getAuthorizationHeader(): Header.Authorization? {
        return request.headers.get(Header.Authorization)
    }

    override suspend fun verify(call: ApplicationCall) {
        val token = call.getAuthorizationHeader()?.value
        if (token.isNullOrEmpty()) {
            throw HttpException(HttpStatusCode.Unauthorized)
        }
    }

}