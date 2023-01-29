package com.kekadoc.project.capybara.server.routing.api.v1.notification

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.repository.notification.NotificationRepository
import com.kekadoc.project.capybara.server.data.repository.user.UserRepository
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.routing.api.v1.profile.requireAuthorizationToken
import com.kekadoc.project.capybara.server.routing.execute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import org.koin.core.component.get

@Serializable
data class AuthNotificationRequest(
    val token: String
)

@Serializable
class AuthNotificationResponse

suspend fun PipelineContext.authNotification() = execute {
    val request = this.call.receive<AuthNotificationRequest>()
    val authToken = requireAuthorizationToken()
    val userRepository = Di.get<UserRepository>()
    val notificationRepository = Di.get<NotificationRepository>()
    val user = userRepository.getUserByToken(authToken).first() ?: throw HttpException(HttpStatusCode.NotFound)
    notificationRepository.savePushToken(user.profile.id, request.token).first()
    val response = AuthNotificationResponse()
    call.respond(response)
}


@Serializable
data class SendNotificationRequest(
    val title: String,
    val body: String,
    val imageUrl: String
)

@Serializable
data class SendNotificationResponse(
    val messageId: String
)

suspend fun PipelineContext.sendNotification() = execute {
    val request = this.call.receive<SendNotificationRequest>()
    val authToken = requireAuthorizationToken()
    val userRepository = Di.get<UserRepository>()
    val notificationRepository = Di.get<NotificationRepository>()
    val user = userRepository.getUserByToken(authToken).first() ?: throw HttpException(HttpStatusCode.NotFound)
    
    val messageId = notificationRepository.sendNotification(
        userId = user.profile.id,
        title = request.title,
        body = request.body,
        imageUrl = request.imageUrl
    ).first()
    
    val response = SendNotificationResponse(messageId = messageId)
    call.respond(response)
}