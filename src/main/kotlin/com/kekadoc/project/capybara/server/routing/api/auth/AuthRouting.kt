package com.kekadoc.project.capybara.server.routing.api.auth

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.intercator.auth.AuthInteractor
import com.kekadoc.project.capybara.server.routing.api.auth.model.AuthorizationRequestDto
import com.kekadoc.project.capybara.server.routing.api.auth.model.RefreshTokensRequestDto
import com.kekadoc.project.capybara.server.routing.api.auth.model.RegistrationRequestDto
import com.kekadoc.project.capybara.server.routing.api.auth.model.UpdateRegistrationStatusRequestDto
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.execution.delete
import com.kekadoc.project.capybara.server.routing.util.execution.post
import com.kekadoc.project.capybara.server.routing.util.requirePathId
import com.kekadoc.project.capybara.server.routing.util.requireQueryParameter
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.get

fun Route.auth() = route("/auth") {
    //Авторизация пользователя
    post<AuthorizationRequestDto> { request -> authorization(request) }

    post<RefreshTokensRequestDto>("/refresh") { request -> refresh(request) }

    route("/registration") {

        post<RegistrationRequestDto> { request -> registration(request) }

        get("/confirm_email") { registrationConfirmEmail() }

        route("/{id}") {

            get { getRegistrationStatus() }

            patch<UpdateRegistrationStatusRequestDto> { request -> updateRegistrationStatus(request) }

            delete { cancelRegistrationRequest() }

        }

        route("/all") {

            get { getAllRegistrationRequests() }

        }

    }
}

suspend fun PipelineContext.authorization(request: AuthorizationRequestDto) = execute(ApiKeyVerifier) {
    val interactor = Di.get<AuthInteractor>()
    val result = interactor.authorize(request)
    call.respond(result)
}

suspend fun PipelineContext.refresh(request: RefreshTokensRequestDto) = execute(ApiKeyVerifier) {
    val interactor = Di.get<AuthInteractor>()
    val result = interactor.refreshToken(request)
    call.respond(result)
}

suspend fun PipelineContext.registration(request: RegistrationRequestDto) = execute(ApiKeyVerifier) {
    val interactor = Di.get<AuthInteractor>()
    val result = interactor.registration(request)
    call.respond(result)
}

suspend fun PipelineContext.getRegistrationStatus() = execute(ApiKeyVerifier) {
    val interactor = Di.get<AuthInteractor>()
    val result = interactor.getRegistrationStatus(requirePathId())
    call.respond(result)
}

suspend fun PipelineContext.cancelRegistrationRequest() = execute(ApiKeyVerifier) {
    val interactor = Di.get<AuthInteractor>()
    val result = interactor.cancelRegistrationRequest(requirePathId())
    call.respond(result)
}

suspend fun PipelineContext.registrationConfirmEmail() = execute {
    val interactor = Di.get<AuthInteractor>()
    val result = interactor.registrationConfirmEmail(requireQueryParameter("token"))
    call.respond(result)
}

suspend fun PipelineContext.updateRegistrationStatus(
    request: UpdateRegistrationStatusRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val interactor = Di.get<AuthInteractor>()
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val result = interactor.updateRegistrationStatus(
        authToken = authToken,
        registrationId = requirePathId(),
        request = request,
    )
    call.respond(result)
}

suspend fun PipelineContext.getAllRegistrationRequests() = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val interactor = Di.get<AuthInteractor>()
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val result = interactor.getAllRegistrationRequests(authToken)
    call.respond(result)
}