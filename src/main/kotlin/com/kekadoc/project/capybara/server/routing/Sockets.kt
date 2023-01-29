package com.kekadoc.project.capybara.server.routing

import com.kekadoc.project.capybara.server.common.http.Header
import com.kekadoc.project.capybara.server.data.repository.message.MessageRepository
import com.kekadoc.project.capybara.server.di.Di
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.get

fun Application.configureSockets() {
    routing {
        //Сокет получения статусов сообщения
        webSocket("/message/status") {
            val authorization = this.call.request.header(Header.Authorization.name).orEmpty()
            if (authorization.isEmpty()) {
                this.close(IllegalStateException("Хуйня"))
            }
            val messageId = call.parameters["id"].orEmpty()
            if (messageId.isEmpty()) {
                close(java.lang.IllegalStateException("Хуйня"))
            }
            val repository = Di.get<MessageRepository>()
            repository.observeMessage(messageId).collectLatest { message ->
                send(Json.encodeToString(message.state))
            }
        }
    }
}