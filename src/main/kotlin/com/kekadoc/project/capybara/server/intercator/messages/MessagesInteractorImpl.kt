package com.kekadoc.project.capybara.server.intercator.messages

import com.kekadoc.project.capybara.server.data.model.Message
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.intercator.orNotFoundError
import com.kekadoc.project.capybara.server.intercator.requireAuthor
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.messages.model.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.websocket.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class MessagesInteractorImpl(
    private val userRepository: UsersRepository,
    private val messagesRepository: MessagesRepository,
) : MessagesInteractor {

    override suspend fun getSentMessages(
        authToken: String,
    ): GetSentMessagesResponse {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user -> messagesRepository.getMessagesByAuthorId(user.id) }
            .map(::GetSentMessagesResponse)
            .single()
    }

    override suspend fun createSentMessage(
        authToken: String,
        request: CreateSentMessageRequest,
    ): CreateSentMessageResponse {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.createMessage(
                    authorId = user.id,
                    addresseeGroups = request.addresseeGroups.toSet(),
                    addresseeUsers = request.addresseeUsers.toSet(),
                    content = request.content,
                )
            }
            .map(::CreateSentMessageResponse)
            .single()
    }

    override suspend fun getSentMessage(
        authToken: String,
        messageId: String,
    ): GetSentMessageResponse {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.getMessage(messageId)
                    .orNotFoundError()
                    .requireAuthor(user)
            }
            .map(::GetSentMessageResponse)
            .single()
    }

    override suspend fun updateSentMessage(
        authToken: String,
        messageId: String,
        request: UpdateSentMessageRequest,
    ): UpdateSentMessageResponse {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.getMessage(messageId)
                    .orNotFoundError()
                    .requireAuthor(user)
                    .map { user }
            }
            .flatMapLatest {
                messagesRepository.updateMessage(
                    messageId = messageId,
                    content = request.content,
                )
            }
            .map(::UpdateSentMessageResponse)
            .single()
    }

    override suspend fun deleteSentMessage(
        authToken: String,
        messageId: String,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.getMessage(messageId)
                    .orNotFoundError()
                    .requireAuthor(user)
            }
            .flatMapLatest {
                messagesRepository.removeMessage(
                    messageId = messageId,
                )
            }
            .single()
    }


    override fun observeSentMessageState(
        authToken: String,
        messageId: String,
    ): Flow<Message.State> {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.observeMessage(messageId)
                    .requireAuthor(user)
                    .map { it.state }
            }
    }

    override suspend fun getReceivedMessages(
        authToken: String,
    ): GetReceivedMessages {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                merge(
                    messagesRepository.getMessagesByAddresseeUserId(user.id),
                    messagesRepository.getMessagesByAddresseeGroupIds(user.groups.toSet()),
                )
            }
            .map(::GetReceivedMessages)
            .single()
    }

    override suspend fun getReceivedMessage(
        authToken: String,
        messageId: String,
    ): GetReceivedMessage {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.getMessage(messageId)
                    .orNotFoundError()
                    .requireAuthor(user)
            }
            .map(::GetReceivedMessage)
            .single()
    }

    override suspend fun setReceivedMessageAnswer(
        authToken: String,
        messageId: String,
        request: PostReceivedMessageAnswerRequest,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun setReceivedMessageNotify(
        authToken: String,
        messageId: String,
        request: PostReceivedMessageNotifyRequest,
    ) {
        TODO("Not yet implemented")
    }



    //@Serializable
//data class SendNotificationRequest(
//    val title: String,
//    val body: String,
//    val imageUrl: String
//)
//
//@Serializable
//data class SendNotificationResponse(
//    val messageId: String
//)
//
//suspend fun PipelineContext.sendNotification() = execute {
//    val request = this.call.receive<SendNotificationRequest>()
//    val authToken = requireAuthorizationToken()
//    val userRepository = Di.get<UserRepository>()
//    val notificationRepository = Di.get<NotificationRepository>()
//    val user = userRepository.getUserByToken(authToken).first() ?: throw HttpException(HttpStatusCode.NotFound)
//
//    val messageId = notificationRepository.sendNotification(
//        userId = user.profile.id,
//        title = request.title,
//        body = request.body,
//        imageUrl = request.imageUrl
//    ).first()
//
//    val response = SendNotificationResponse(messageId = messageId)
//    call.respond(response)
//}

}