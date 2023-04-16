package com.kekadoc.project.capybara.server.routing.api.messages

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.intercator.messages.MessagesInteractor
import com.kekadoc.project.capybara.server.routing.api.messages.model.CreateSentMessageRequest
import com.kekadoc.project.capybara.server.routing.api.messages.model.PostReceivedMessageAnswerRequest
import com.kekadoc.project.capybara.server.routing.api.messages.model.UpdateSentMessageRequest
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.executeAuthorizedApi
import com.kekadoc.project.capybara.server.routing.util.requirePathId
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.get

fun Route.messages() = route("/messages") {

    route("/sent") {

        //Получение списка всех отправленных сообщений
        get { getSentMessages() }

        //Создание сообщения
        post<CreateSentMessageRequest> { request -> createSentMessage(request) }

        //Оперции с отправленным сообщением
        route("/{id}") {
            //Получение детальной информации о сообщении
            get { getSentMessage(requirePathId()) }
            //Обновление сообщения
            patch<UpdateSentMessageRequest> { request -> updateSentMessage(requirePathId(), request) }
            //Удалить сообщение
            delete { deleteSentMessage(requirePathId()) }

            //Сокет получения статусов сообщения
            webSocket("/status") { observeMessageStatus(requirePathId()) }
        }

    }

    route("/received") {

        //Получение списка всех сообщений для пользователя
        get { getReceivedMessages() }

        //Оперции с полученным сообщением
        route("/{id}") {

            //Получение детальной информации о сообщении
            get { getReceivedMessage(requirePathId()) }

            //Отправка ответа на сообщение
            post<PostReceivedMessageAnswerRequest>("/answer") { answerReceivedMessage(requirePathId()) }

            //Обновление статуса нотификации от получателя
            post<PostReceivedMessageAnswerRequest>("/notify") { notifyReceivedMessages(requirePathId()) }

        }

    }

}

private suspend fun PipelineContext.getSentMessages() = execute(
    ApiKeyVerifier, AuthorizationVerifier
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()
    val result = interactor.getSentMessages(authToken)
    call.respond(result)
}

private suspend fun PipelineContext.getSentMessage(messageId: String) = execute(
    ApiKeyVerifier, AuthorizationVerifier
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()
    val result = interactor.getSentMessage(
        authToken = authToken,
        messageId = messageId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.createSentMessage(
    request: CreateSentMessageRequest
) = execute {
        val authToken = AuthorizationVerifier.requireAuthorizationToken()
        val interactor = Di.get<MessagesInteractor>()
        val result = interactor.createSentMessage(
            authToken = authToken,
            request = request,
        )
        call.respond(result)
    }

private suspend fun PipelineContext.updateSentMessage(
    messageId: String,
    request: UpdateSentMessageRequest,
) = execute {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()
    val result = interactor.updateSentMessage(
        authToken = authToken,
        messageId = messageId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.deleteSentMessage(
    messageId: String,
) = execute {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()
    val result = interactor.deleteSentMessage(
        authToken = authToken,
        messageId = messageId,
    )
    call.respond(result)
}

private suspend fun DefaultWebSocketServerSession.observeMessageStatus(
    messageId: String,
) = executeAuthorizedApi {

    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()
    interactor.observeSentMessageState(
        authToken = authToken,
        messageId = messageId,
    ).onEach { state ->
        send(Json.encodeToString(state))
    }.collect()

}

private suspend fun PipelineContext.getReceivedMessages() = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()
    val result = interactor.getReceivedMessages(
        authToken = authToken,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getReceivedMessage(
    messageId: String,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()
    val result = interactor.getReceivedMessage(
        authToken = authToken,
        messageId = messageId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.answerReceivedMessage(
    messageId: String,
) = executeAuthorizedApi { TODO() }

private suspend fun PipelineContext.notifyReceivedMessages(
    messageId: String,
) = executeAuthorizedApi { TODO() }

