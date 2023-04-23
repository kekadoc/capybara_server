package com.kekadoc.project.capybara.server.intercator.notification

import com.kekadoc.project.capybara.server.data.model.Group
import com.kekadoc.project.capybara.server.data.repository.notification.NotificationRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.converter.dto.NotificationDtoConverter
import com.kekadoc.project.capybara.server.intercator.orNotFoundError
import com.kekadoc.project.capybara.server.intercator.requireAuthor
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.notifications.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationsInteractorImpl(
    private val userRepository: UsersRepository,
    private val messagesRepository: NotificationRepository,
) : NotificationsInteractor {

    override suspend fun getSentNotifications(
        authToken: String,
    ): GetSentNotificationsResponse {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user -> messagesRepository.getMessagesByAuthorId(user.id) }
            .map { it.map(NotificationDtoConverter::convert) }
            .map(::GetSentNotificationsResponse)
            .single()
    }

    override suspend fun createSentNotification(
        authToken: String,
        request: CreateSentNotificationRequest,
    ): CreateSentNotificationResponse {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.createNotification(
                    authorId = user.id,
                    addresseeGroups = request.addresseeGroups.toSet(),
                    addresseeUsers = request.addresseeUsers.toSet(),
                    content = request.content.let(NotificationDtoConverter.ContentConverter::revert),
                    type = request.type.let(NotificationDtoConverter.TypeConverter::revert),
                )
            }
            .map(NotificationDtoConverter::convert)
            .map(::CreateSentNotificationResponse)
            .single()
    }

    override suspend fun getSentNotification(
        authToken: String,
        messageId: String,
    ): GetSentNotificationResponse {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.getMessage(messageId)
                    .orNotFoundError()
                    .requireAuthor(user)
                    .map(NotificationDtoConverter::convert)
            }
            .map(::GetSentNotificationResponse)
            .single()
    }

    override suspend fun updateSentNotification(
        authToken: String,
        messageId: String,
        request: UpdateSentMessageRequest,
    ): UpdateSentNotificationResponse {
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
                    content = NotificationDtoConverter.ContentConverter.revert(request.content),
                )
            }
            .map(NotificationDtoConverter::convert)
            .map(::UpdateSentNotificationResponse)
            .single()
    }

    override suspend fun deleteSentNotification(
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


//    override fun observeSentMessageState(
//        authToken: String,
//        messageId: String,
//    ): Flow<Message.State> {
//        return userRepository.getUserByToken(authToken)
//            .requireAuthorizedUser()
//            .flatMapLatest { user ->
//                messagesRepository.observeMessage(messageId)
//                    .requireAuthor(user)
//                    .map { it.state }
//            }
//    }

    override suspend fun getReceivedNotifications(
        authToken: String,
    ): GetReceivedNotifications {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                merge(
                    messagesRepository.getMessagesByAddresseeUserId(user.id),
                    messagesRepository.getMessagesByAddresseeGroupIds(user.groupIds.toSet()),
                )
            }
            .map { it.map(NotificationDtoConverter::convert) }
            .map(::GetReceivedNotifications)
            .single()
    }

    override suspend fun getReceivedNotification(
        authToken: String,
        messageId: String,
    ): GetReceivedNotification {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.getMessage(messageId)
                    .orNotFoundError()
                    .requireAuthor(user)
            }
            .map(NotificationDtoConverter::convert)
            .map(::GetReceivedNotification)
            .single()
    }

    override suspend fun setReceivedNotificationAnswer(
        authToken: String,
        messageId: String,
        request: PostReceivedMessageAnswerRequest,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun setReceivedNotificationNotify(
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