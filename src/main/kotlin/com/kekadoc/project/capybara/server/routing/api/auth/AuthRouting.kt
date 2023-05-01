package com.kekadoc.project.capybara.server.routing.api.auth

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.intercator.auth.AuthInteractor
import com.kekadoc.project.capybara.server.routing.api.auth.model.AuthorizationRequest
import com.kekadoc.project.capybara.server.routing.api.auth.model.RefreshTokensRequest
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.execution.post
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.get

fun Route.auth() = route("/auth") {
    //Авторизация пользователя
    post<AuthorizationRequest> { request -> authorization(request) }

    post<RefreshTokensRequest>("/refresh") { request -> refresh(request) }
}

suspend fun PipelineContext.authorization(request: AuthorizationRequest) = execute(ApiKeyVerifier) {
    val interactor = Di.get<AuthInteractor>()
    val result = interactor.authorize(request).getOrThrow()
    call.respond(result)
}

suspend fun PipelineContext.refresh(request: RefreshTokensRequest) = execute(ApiKeyVerifier) {
    val interactor = Di.get<AuthInteractor>()
    val result = interactor.refreshToken(request).getOrThrow()
    call.respond(result)
}
