package com.kekadoc.project.capybara.server.intercator.notification

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.NotificationInfo
import com.kekadoc.project.capybara.server.data.model.Token
import com.kekadoc.project.capybara.server.data.repository.distribution.DistributionRepository
import com.kekadoc.project.capybara.server.data.repository.notification.NotificationRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.converter.dto.NotificationDtoConverter
import com.kekadoc.project.capybara.server.data.source.factory.dto.NotificationInfoDtoFactory
import com.kekadoc.project.capybara.server.intercator.*
import com.kekadoc.project.capybara.server.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.routing.api.notifications.model.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationsInteractorImpl(
    private val userRepository: UsersRepository,
    private val messagesRepository: NotificationRepository,
    private val distributionRepository: DistributionRepository,
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
) : NotificationsInteractor {

    override suspend fun getSentNotifications(
        authToken: Token,
    ): GetSentNotificationsResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user -> messagesRepository.getNotificationsByAuthorId(user.id) }
        .map { it.map(NotificationDtoConverter::convert) }
        .map(::GetSentNotificationsResponse)
        .single()

    override suspend fun createSentNotification(
        authToken: Token,
        request: CreateSentNotificationRequest,
    ): CreateSentNotificationResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireSpeakerUser()
        .flatMapLatest { user ->
            val isAllUsersAvailableForNotificationFlow = userRepository.getAccessForUsers(
                userId = user.id,
                forUserIds = request.addresseeUsers,
            ).onEach { accessForUsers ->
                accessForUsers.forEach { access ->
                    if (!access.sentNotification) throw HttpException(
                        statusCode = HttpStatusCode.Forbidden,
                        message = "The user {${access.toUserId}} is not available for notification"
                    )
                }
            }
            val isAllGroupsAvailableForNotificationFlow = userRepository.getAccessForGroup(
                userId = user.id,
                groupIds = request.addresseeGroups,
            ).onEach { accessForGroups ->
                accessForGroups.forEach { access ->
                    if (!access.sentNotification) throw HttpException(
                        statusCode = HttpStatusCode.Forbidden,
                        message = "The group {${access.groupId}} is not available for notification"
                    )
                }
            }
            combine(
                isAllUsersAvailableForNotificationFlow,
                isAllGroupsAvailableForNotificationFlow,
            ) { _, _ -> }
                .flatMapLatest {
                    messagesRepository.createNotification(
                        authorId = user.id,
                        addresseeGroups = request.addresseeGroups.toSet(),
                        addresseeUsers = request.addresseeUsers.toSet(),
                        content = request.content.let(NotificationDtoConverter.ContentConverter::revert),
                        type = request.type.let(NotificationDtoConverter.TypeConverter::revert),
                    )
                }
                .flatMapLatest { notification ->
                    distributionRepository.distribute(notification).map { notification }
                }
        }
        .map(NotificationDtoConverter::convert)
        .map(::CreateSentNotificationResponse)
        .single()

    override suspend fun getSentNotification(
        authToken: Token,
        notificationId: Identifier,
    ): GetSentNotificationResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getNotificationInfo(notificationId)
                .map { it ?: throw HttpException(HttpStatusCode.NotFound) }
                .onEach { info ->
                    if (info.notification.authorId != user.id) {
                        throw HttpException(HttpStatusCode.Forbidden)
                    }
                }
        }
        .map(NotificationInfoDtoFactory::create)
        .map(::GetSentNotificationResponse)
        .single()

    override suspend fun updateSentNotification(
        authToken: Token,
        notificationId: Identifier,
        request: UpdateSentMessageRequest,
    ): UpdateSentNotificationResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getNotification(notificationId)
                .orNotFoundError()
                .requireAuthor(user)
                .map { user }
        }
        .flatMapLatest {
            messagesRepository.updateNotification(
                messageId = notificationId,
                content = NotificationDtoConverter.ContentConverter.revert(request.content),
            )
        }
        .map { value -> value ?: throw HttpException(HttpStatusCode.NotFound) }
        .map(NotificationDtoConverter::convert)
        .map(::UpdateSentNotificationResponse)
        .single()

    override suspend fun deleteSentNotification(
        authToken: Token,
        notificationId: Identifier,
    ) {
        fetchUserByAccessTokenFunction.fetchUser(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.getNotification(notificationId)
                    .orNotFoundError()
                    .requireAuthor(user)
            }
            .flatMapLatest {
                messagesRepository.removeNotification(
                    messageId = notificationId,
                )
            }
            .single()
    }

    override suspend fun getReceivedNotifications(
        authToken: Token,
    ): GetReceivedNotifications = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            combine(
                messagesRepository.getNotificationsByAddresseeUserId(user.id),
                messagesRepository.getNotificationsByAddresseeGroupIds(user.groupIds.toSet()),
            ) { messagesByAddresseeUser, messagesByAddresseeGroup ->
                messagesByAddresseeUser + messagesByAddresseeGroup
            }
        }
        .map { it.map(NotificationDtoConverter::convert) }
        .map(::GetReceivedNotifications)
        .single()

    override suspend fun getReceivedNotification(
        authToken: Token,
        notificationId: Identifier,
    ): GetReceivedNotification = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getNotification(notificationId)
                .orNotFoundError()
                .requireAddressee(user)
        }
        .map(NotificationDtoConverter::convert)
        .map(::GetReceivedNotification)
        .single()

    override suspend fun setReceivedNotificationAnswer(
        authToken: Token,
        notificationId: Identifier,
        request: PostReceivedMessageAnswerRequest,
    ): Unit = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getNotification(notificationId)
                .orNotFoundError()
                .requireAddressee(user)
                .flatMapLatest {
                    messagesRepository.setReceivedNotificationAnswer(
                        notificationId = notificationId,
                        userId = user.id,
                        request = request,
                    )
                }
                .orNotFoundError()
        }
        .collect()

    override suspend fun setReceivedNotificationNotify(
        authToken: Token,
        notificationId: Identifier
    ): Unit = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getNotification(notificationId)
                .orNotFoundError()
                .requireAddressee(user)
                .flatMapLatest {
                    messagesRepository.setReceivedNotificationNotify(
                        notificationId = notificationId,
                        userId = user.id,
                    )
                }
                .orNotFoundError()
        }
        .collect()

    override suspend fun setReadNotificationNotify(
        authToken: Token,
        notificationId: Identifier,
    ): Unit = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getNotification(notificationId)
                .orNotFoundError()
                .requireAddressee(user)
                .flatMapLatest {
                    messagesRepository.setReadNotificationNotify(
                        notificationId = notificationId,
                        userId = user.id,
                    )
                }
                .orNotFoundError()
        }
        .collect()


    //@Serializable
//data class SendNotificationRequest(
//    val title: String,
//    val body: String,
//    val imageUrl: String
//)
//
//@Serializable
//data class SendNotificationResponse(
//    val messageId: Identifier
//)
//
//suspend fun PipelineContext.sendNotification() = execute {
//    val request = this.call.receive<SendNotificationRequest>()
//    val authToken = requireAuthorizationToken()
//    val userRepository = Di.get<UserRepository>()
//    val notificationRepository = Di.get<NotificationRepository>()
//    val user = fetchUserByAccessTokenFunction.fetchUser(authToken).first() ?: throw HttpException(HttpStatusCode.NotFound)
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