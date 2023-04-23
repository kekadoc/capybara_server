package com.kekadoc.project.capybara.server.routing.api.notifications.mobile

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.intercator.mobile_push.MobilePushInteractor
import com.kekadoc.project.capybara.server.routing.api.notifications.mobile.model.DeletePushTokenByUserIdRequest
import com.kekadoc.project.capybara.server.routing.api.profile.model.UpdatePushTokenRequest
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.execution.delete
import com.kekadoc.project.capybara.server.routing.util.execution.post
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.get

fun Route.mobileNotifications() = route("/mobile") {

    route("/push") {

        //Обновление пуш токена через токен авторизации
        post<UpdatePushTokenRequest> { request -> updatePushTokenByAuthToken(request) }

        //Удаление пуш токена через токен авторизации
        delete { deletePushTokenByAuthToken() }

        //Удаление пуштокена по идентификатору пользователя
        post<DeletePushTokenByUserIdRequest> { request -> deletePushTokenById(request.userId) }

    }

}

private suspend fun PipelineContext.updatePushTokenByAuthToken(
    request: UpdatePushTokenRequest,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MobilePushInteractor>()
    val result = interactor.updatePushTokenByAuthToken(
        authToken = authToken,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.deletePushTokenByAuthToken() = execute(
    ApiKeyVerifier,
    AuthorizationVerifier
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MobilePushInteractor>()
    val result = interactor.deletePushTokenByAuthToken(
        authToken = authToken,
    )
    call.respond(result)
}

private suspend fun PipelineContext.deletePushTokenById(
    profileId: String,
) = execute(
    ApiKeyVerifier,
    AuthorizationVerifier
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MobilePushInteractor>()
    val result = interactor.deletePushTokenById(
        authToken = authToken,
        profileId = profileId,
    )
    call.respond(result)
}