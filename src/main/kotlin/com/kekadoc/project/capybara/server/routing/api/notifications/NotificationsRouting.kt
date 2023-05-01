package com.kekadoc.project.capybara.server.routing.api.notifications

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.intercator.notification.NotificationsInteractor
import com.kekadoc.project.capybara.server.routing.api.notifications.mobile.mobileNotifications
import com.kekadoc.project.capybara.server.routing.api.notifications.model.CreateSentNotificationRequest
import com.kekadoc.project.capybara.server.routing.api.notifications.model.PostReceivedMessageAnswerRequest
import com.kekadoc.project.capybara.server.routing.api.notifications.model.UpdateSentMessageRequest
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.executeAuthorizedApi
import com.kekadoc.project.capybara.server.routing.util.requirePathId
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.core.component.get

fun Route.notifications() = route("/notifications") {

    route("/sent") {

        //Получение списка всех отправленных сообщений
        get { getSentNotifications() }

        //Создание сообщения
        post<CreateSentNotificationRequest> { request -> createSentNotification(request) }

        //Оперции с отправленным сообщением
        route("/{id}") {

            //Получение детальной информации о сообщении
            get { getSentNotification(requirePathId()) }

            //Обновление сообщения
            patch<UpdateSentMessageRequest> { request -> updateSentNotification(requirePathId(), request) }

            //Удалить сообщение
            delete { deleteSentNotification(requirePathId()) }

            //Сокет получения статусов сообщения
            webSocket("/status") { observeNotificationStatus(requirePathId()) }

        }

    }

    route("/received") {

        //Получение списка всех сообщений для пользователя
        get { getReceivedNotification() }

        //Оперции с полученным сообщением
        route("/{id}") {

            //Получение детальной информации о сообщении
            get { getReceivedNotification(requirePathId()) }

            //Отправка ответа на сообщение
            post<PostReceivedMessageAnswerRequest>("/answer") { request ->
                answerReceivedNotification(
                    messageId = requirePathId(),
                    request = request,
                )
            }

            //Сообщение о том что сообщение получено
            patch("/notify_received") { notifyReceivedNotification(requirePathId()) }

            //Сообщение о том что сообщение прочитано
            patch("/notify_read") { notifyReadNotification(requirePathId()) }
        }

    }

    mobileNotifications()

}

private suspend fun PipelineContext.getSentNotifications() = execute(
    ApiKeyVerifier, AuthorizationVerifier
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<NotificationsInteractor>()
    val result = interactor.getSentNotifications(authToken)
    call.respond(result)
}

private suspend fun PipelineContext.getSentNotification(messageId: Identifier) = execute(
    ApiKeyVerifier, AuthorizationVerifier
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<NotificationsInteractor>()
    val result = interactor.getSentNotification(
        authToken = authToken,
        notificationId = messageId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.createSentNotification(
    request: CreateSentNotificationRequest
) = execute {
        val authToken = AuthorizationVerifier.requireAuthorizationToken()
        val interactor = Di.get<NotificationsInteractor>()
        val result = interactor.createSentNotification(
            authToken = authToken,
            request = request,
        )
        call.respond(result)
    }

private suspend fun PipelineContext.updateSentNotification(
    messageId: Identifier,
    request: UpdateSentMessageRequest,
) = execute {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<NotificationsInteractor>()
    val result = interactor.updateSentNotification(
        authToken = authToken,
        notificationId = messageId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.deleteSentNotification(
    messageId: Identifier,
) = execute {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<NotificationsInteractor>()
    val result = interactor.deleteSentNotification(
        authToken = authToken,
        notificationId = messageId,
    )
    call.respond(result)
}

private suspend fun DefaultWebSocketServerSession.observeNotificationStatus(
    messageId: Identifier,
) = executeAuthorizedApi {

    throw NotImplementedError()

//    val authToken = AuthorizationVerifier.requireAuthorizationToken()
//    val interactor = Di.get<MessagesInteractor>()
//    interactor.observeSentMessageState(
//        authToken = authToken,
//        messageId = messageId,
//    ).onEach { state ->
//        send(Json.encodeToString(state))
//    }.collect()

}

private suspend fun PipelineContext.getReceivedNotification() = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<NotificationsInteractor>()
    val result = interactor.getReceivedNotifications(
        authToken = authToken,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getReceivedNotification(
    messageId: Identifier,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<NotificationsInteractor>()
    val result = interactor.getReceivedNotification(
        authToken = authToken,
        notificationId = messageId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.answerReceivedNotification(
    messageId: Identifier,
    request: PostReceivedMessageAnswerRequest,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<NotificationsInteractor>()
    val result = interactor.setReceivedNotificationAnswer(
        authToken = authToken,
        notificationId = messageId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.notifyReceivedNotification(
    messageId: Identifier,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<NotificationsInteractor>()
    val result = interactor.setReceivedNotificationNotify(
        authToken = authToken,
        notificationId = messageId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.notifyReadNotification(
    messageId: Identifier,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<NotificationsInteractor>()
    val result = interactor.setReadNotificationNotify(
        authToken = authToken,
        notificationId = messageId,
    )
    call.respond(result)
}


