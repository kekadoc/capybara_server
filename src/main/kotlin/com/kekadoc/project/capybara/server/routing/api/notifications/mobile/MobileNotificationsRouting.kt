package com.kekadoc.project.capybara.server.routing.api.notifications.mobile

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.intercator.mobile_push.MobilePushInteractor
import com.kekadoc.project.capybara.server.routing.api.notifications.mobile.model.UpdatePushTokenByUserIdRequestDto
import com.kekadoc.project.capybara.server.routing.api.notifications.mobile.model.UpdatePushTokenRequest
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.execution.delete
import com.kekadoc.project.capybara.server.routing.util.execution.get
import com.kekadoc.project.capybara.server.routing.util.execution.patch
import com.kekadoc.project.capybara.server.routing.util.execution.post
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.get

fun Route.mobileNotifications() = route("/mobile") {

    route("/push") {

        get { getPushTokenByAuth() }

        //Обновление пуш токена через токен авторизации
        post<UpdatePushTokenRequest> { request -> updatePushTokenByAuth(request) }

        //Удаление пуш токена через токен авторизации
        delete { deletePushTokenByAuth() }

        //Обновление пуштокена по идентификатору пользователя
        patch<UpdatePushTokenByUserIdRequestDto> { request -> updatePushTokenByUserId(request) }

    }

}

private suspend fun PipelineContext.getPushTokenByAuth(

) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MobilePushInteractor>()
    val result = interactor.getPushTokenByAuth(
        authToken = authToken,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updatePushTokenByAuth(
    request: UpdatePushTokenRequest,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MobilePushInteractor>()
    val result = interactor.updatePushTokenByAuth(
        authToken = authToken,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.deletePushTokenByAuth() = execute(
    ApiKeyVerifier,
    AuthorizationVerifier
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MobilePushInteractor>()
    val result = interactor.deletePushTokenByAuth(
        authToken = authToken,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updatePushTokenByUserId(
    request: UpdatePushTokenByUserIdRequestDto,
) = execute(
    ApiKeyVerifier,
    AuthorizationVerifier
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MobilePushInteractor>()
    val result = interactor.updatePushTokenByUserId(
        authToken = authToken,
        userId = request.userId,
        pushToken = request.token,
    )
    call.respond(result)
}