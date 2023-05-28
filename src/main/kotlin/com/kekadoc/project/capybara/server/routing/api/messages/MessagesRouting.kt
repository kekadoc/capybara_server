package com.kekadoc.project.capybara.server.routing.api.messages

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.intercator.notification.MessagesInteractor
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.routing.api.messages.model.CreateMessageRequestDto
import com.kekadoc.project.capybara.server.routing.api.messages.model.UpdateReceivedMessageAnswerRequestDto
import com.kekadoc.project.capybara.server.routing.model.RangeDto
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.executeAuthorizedApi
import com.kekadoc.project.capybara.server.routing.util.requirePathId
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.core.component.get

fun Route.messages() = route("/messages") {

    route("/sent") {

        //Получение списка всех отправленных сообщений
        get { getSentMessages() }

        //Создание сообщения
        post<CreateMessageRequestDto> { request -> createMessage(request) }

        //Оперции с отправленным сообщением
        route("/{id}") {

            //Получение детальной информации о сообщении
            get { getSentMessage(requirePathId()) }

            //Удалить сообщение
            delete { deleteSentMessage(requirePathId()) }

            //Сокет получения статусов сообщения
            webSocket("/status") { observeNotificationStatus(requirePathId()) }

        }

    }

    route("/received") {

        //Получение списка всех сообщений для пользователя
        get { getReceivedMessage() }

        //Оперции с полученным сообщением
        route("/{id}") {

            //Получение детальной информации о сообщении
            get { getReceivedMessage(requirePathId()) }

            //Отправка ответа на сообщение
            post<UpdateReceivedMessageAnswerRequestDto>("/answer") { request ->
                answerReceivedMessage(
                    messageId = requirePathId(),
                    request = request,
                )
            }

            //Сообщение о том что сообщение получено
            patch("/notify_received") { notifyReceivedMessage(requirePathId()) }

            //Сообщение о том что сообщение прочитано
            patch("/notify_read") { notifyReadMessage(requirePathId()) }

        }

    }

}

private suspend fun PipelineContext.getSentMessages() = execute(
    ApiKeyVerifier, AuthorizationVerifier,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()

    val from = call.request.queryParameters["from"]?.toIntOrNull()
    val count = call.request.queryParameters["count"]?.toIntOrNull()

    if (from == null || count == null) {
        throw HttpException(HttpStatusCode.BadRequest, "range not found")
    }
    val range = RangeDto(
        from = from,
        count = count,
    )
    val result = interactor.getSentMessages(authToken, range)
    call.respond(result)
}

private suspend fun PipelineContext.getSentMessage(messageId: Identifier) = execute(
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

private suspend fun PipelineContext.createMessage(
    request: CreateMessageRequestDto,
) = execute {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()
    val result = interactor.createMessage(
        authToken = authToken,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.deleteSentMessage(
    messageId: Identifier,
) = execute {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()
    val result = interactor.deleteSentMessage(
        authToken = authToken,
        messageId = messageId,
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

private suspend fun PipelineContext.getReceivedMessage() = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()

    val from = call.request.queryParameters["from"]?.toIntOrNull()
    val count = call.request.queryParameters["count"]?.toIntOrNull()

    if (from == null || count == null) {
        throw HttpException(HttpStatusCode.BadRequest, "range not found")
    }
    val range = RangeDto(
        from = from,
        count = count,
    )

    val result = interactor.getReceivedMessages(
        authToken = authToken,
        range = range,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getReceivedMessage(
    messageId: Identifier,
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
    messageId: Identifier,
    request: UpdateReceivedMessageAnswerRequestDto,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()
    val result = interactor.setReceivedMessageAnswer(
        authToken = authToken,
        messageId = messageId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.notifyReceivedMessage(
    messageId: Identifier,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()
    val result = interactor.setReceivedMessageNotify(
        authToken = authToken,
        messageId = messageId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.notifyReadMessage(
    messageId: Identifier,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<MessagesInteractor>()
    val result = interactor.setReadMessageNotify(
        authToken = authToken,
        messageId = messageId,
    )
    call.respond(result)
}


