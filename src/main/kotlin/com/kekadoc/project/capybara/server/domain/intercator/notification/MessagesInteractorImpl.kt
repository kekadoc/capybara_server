package com.kekadoc.project.capybara.server.domain.intercator.notification

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.extensions.mapElements
import com.kekadoc.project.capybara.server.common.extensions.orElse
import com.kekadoc.project.capybara.server.data.manager.message_with_notification.MessageWithNotificationManager
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.network.model.converter.MessageDtoConverter
import com.kekadoc.project.capybara.server.data.source.network.model.factory.MessageInfoDtoFactory
import com.kekadoc.project.capybara.server.domain.intercator.*
import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Message
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.messages.model.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class MessagesInteractorImpl(
    private val messageWithNotificationManager: MessageWithNotificationManager,
    private val userRepository: UsersRepository,
    private val messagesRepository: MessagesRepository,
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
) : MessagesInteractor {

    override suspend fun getSentMessages(
        authToken: Token,
    ): GetSentNotificationsResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user -> messagesRepository.getMessagesByAuthorId(user.id) }
        .mapElements(MessageDtoConverter::convert)
        .map(::GetSentNotificationsResponseDto)
        .single()

    override suspend fun createMessage(
        authToken: Token,
        request: CreateMessageRequestDto,
    ): CreateNotificationResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
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
                    messageWithNotificationManager.sentMessage(
                        type = request.type.let(MessageDtoConverter.TypeConverter::revert),
                        authorId = user.id,
                        addresseeGroups = request.addresseeGroups.toSet(),
                        addresseeUsers = request.addresseeUsers.toSet(),
                        content = request.content.let(MessageDtoConverter.ContentConverter::revert),
                        actions = request.actions.let(MessageDtoConverter.ActionsConverter::revert),
                        notifications = request.notifications
                            ?.let(MessageDtoConverter.NotificationsConverter::revert)
                            .orElse { Message.Notifications.Default },
                    )
                }
        }
        .map(MessageDtoConverter::convert)
        .map(::CreateNotificationResponseDto)
        .single()

    override suspend fun getSentMessage(
        authToken: Token,
        messageId: Identifier,
    ): GetSentMessageResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getMessageInfo(messageId)
                .onEach { info ->
                    if (info.message.authorId != user.id) {
                        throw HttpException(HttpStatusCode.Forbidden)
                    }
                }
        }
        .map(MessageInfoDtoFactory::create)
        .map(::GetSentMessageResponseDto)
        .single()

    override suspend fun updateSentMessage(
        authToken: Token,
        messageId: Identifier,
        request: UpdateSentMessageRequestDto,
    ): UpdateSentMessageResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
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
                content = MessageDtoConverter.ContentConverter.revert(request.content),
            )
        }
        .map(MessageDtoConverter::convert)
        .map(::UpdateSentMessageResponseDto)
        .single()

    override suspend fun deleteSentMessage(
        authToken: Token,
        messageId: Identifier,
    ) {
        fetchUserByAccessTokenFunction.fetchUser(authToken)
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

    override suspend fun getReceivedMessages(
        authToken: Token,
    ): GetReceivedMessagesDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            combine(
                messagesRepository.getMessagesByAddresseeUserId(user.id),
                messagesRepository.getMessagesByAddresseeGroupIds(user.groupIds.toSet()),
            ) { messagesByAddresseeUser, messagesByAddresseeGroup ->
                messagesByAddresseeUser + messagesByAddresseeGroup
            }
        }
        .mapElements(MessageDtoConverter::convert)
        .map(::GetReceivedMessagesDto)
        .single()

    override suspend fun getReceivedMessage(
        authToken: Token,
        messageId: Identifier,
    ): GetReceivedNotificationDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getMessage(messageId)
                .orNotFoundError()
                .requireAddressee(user)
        }
        .map(MessageDtoConverter::convert)
        .map(::GetReceivedNotificationDto)
        .single()

    override suspend fun setReceivedMessageAnswer(
        authToken: Token,
        messageId: Identifier,
        request: PostReceivedMessageAnswerRequestDto,
    ): Unit = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getMessage(messageId)
                .orNotFoundError()
                .requireAddressee(user)
                .flatMapLatest {
                    messagesRepository.setReceivedMessageAnswer(
                        messageId = messageId,
                        userId = user.id,
                        answer = request.answer,
                    )
                }
                .orNotFoundError()
        }
        .collect()

    override suspend fun setReceivedMessageNotify(
        authToken: Token,
        messageId: Identifier
    ): Unit = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getMessage(messageId)
                .orNotFoundError()
                .requireAddressee(user)
                .flatMapLatest {
                    messagesRepository.setReceivedMessageNotify(
                        messageId = messageId,
                        userId = user.id,
                    )
                }
                .orNotFoundError()
        }
        .collect()

    override suspend fun setReadMessageNotify(
        authToken: Token,
        messageId: Identifier,
    ): Unit = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getMessage(messageId)
                .orNotFoundError()
                .requireAddressee(user)
                .flatMapLatest {
                    messagesRepository.setReadMessageNotify(
                        messageId = messageId,
                        userId = user.id,
                    )
                }
                .orNotFoundError()
        }
        .collect()

}