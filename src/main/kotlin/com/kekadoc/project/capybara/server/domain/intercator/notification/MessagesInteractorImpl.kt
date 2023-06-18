package com.kekadoc.project.capybara.server.domain.intercator.notification

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.manager.message_with_notification.MessageWithNotificationManager
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.domain.intercator.*
import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.intercator.functions.GetReceivedMessageFunction
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.message.MessageAction
import com.kekadoc.project.capybara.server.domain.model.message.MessageNotifications
import com.kekadoc.project.capybara.server.domain.model.message.MessageType
import com.kekadoc.project.capybara.server.domain.model.user.User
import com.kekadoc.project.capybara.server.routing.api.messages.model.*
import com.kekadoc.project.capybara.server.routing.model.RangeDto
import com.kekadoc.project.capybara.server.routing.model.converter.RangeDtoConverter
import com.kekadoc.project.capybara.server.routing.model.factory.SentMessageInfoDtoFactory
import com.kekadoc.project.capybara.server.routing.model.factory.SentMessagePreviewDtoFactory
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class MessagesInteractorImpl(
    private val messageWithNotificationManager: MessageWithNotificationManager,
    private val userRepository: UsersRepository,
    private val messagesRepository: MessagesRepository,
    private val groupsRepository: GroupsRepository,
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
    private val getReceivedMessageFunction: GetReceivedMessageFunction,
) : MessagesInteractor {

    override suspend fun getSentMessages(
        authToken: Token,
        range: RangeDto,
    ): GetSentMessagesResponseDto =
        fetchUserByAccessTokenFunction.fetchUser(authToken).requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.getMessagesByAuthorId(
                    authorId = user.id,
                    range = RangeDtoConverter.convert(range),
                ).map { messages ->
                        coroutineScope {
                            messages.map { message ->
                                async {
                                    val messageInfo = messagesRepository.getMessageInfo(message.id)
                                        .single()
                                    SentMessagePreviewDtoFactory.invoke(message, messageInfo)
                                }
                            }.awaitAll()
                        }
                    }
            }.map(::GetSentMessagesResponseDto).single()

    override suspend fun createMessage(
        authToken: Token,
        request: CreateMessageRequestDto,
    ): CreateMessageResponseDto =
        fetchUserByAccessTokenFunction.fetchUser(authToken)
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
                    groupIds = request.addresseeGroups.map(CreateMessageRequestDto.AddresseeGroup::groupId),
                ).onEach { accessForGroups ->
                    accessForGroups.forEach { access ->
                        if (!access.sentNotification) throw HttpException(
                            statusCode = HttpStatusCode.Forbidden,
                            message = "The group {${access.groupId}} is not available for notification"
                        )
                    }
                }
                val isGroupMembersOk = request.addresseeGroups.filter { it.membersIds != null }
                    .map { (groupId, members) ->
                        groupsRepository.getGroup(groupId).onEach { group ->
                            val containsAll = group.members.map(User::id)
                                .containsAll(members.orEmpty())
                            if (!containsAll) throw HttpException(
                                statusCode = HttpStatusCode.BadRequest,
                                message = "Unknown members in group ${group.id}",
                            )
                        }
                    }.merge().map { Unit }.onEmpty { emit(Unit) }

                combine(
                    isAllUsersAvailableForNotificationFlow,
                    isAllGroupsAvailableForNotificationFlow,
                    isGroupMembersOk,
                ) { _, _, _ -> }.flatMapLatest {
                    messageWithNotificationManager.sentMessage(
                        type = MessageType.valueOf(request.type.name),
                        author = user,
                        addresseeGroups = request.addresseeGroups.associate { (groupId, membersIds) -> groupId to membersIds },
                        addresseeUsers = request.addresseeUsers,
                        title = request.title,
                        text = request.text,
                        actions = request.actions?.mapIndexed { index, text ->
                            MessageAction(
                                id = index.toLong(),
                                text = text
                            )
                        },
                        isMultiAction = request.isMultiAnswer,
                        notifications = request.notifications?.let {
                            MessageNotifications(
                                email = it.email,
                                app = it.app,
                                messengers = it.messengers,
                            )
                        } ?: MessageNotifications.Default,
                    )
                }.map { message ->
                    val messageInfo = messagesRepository.getMessageInfo(message.id).single()
                    SentMessagePreviewDtoFactory.invoke(message, messageInfo)
                }
            }.map(::CreateMessageResponseDto).single()

    override suspend fun getSentMessage(
        authToken: Token,
        messageId: Identifier,
    ): GetSentMessageResponseDto =
        fetchUserByAccessTokenFunction.fetchUser(authToken).requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.getMessageInfo(messageId).onEach { info ->
                        if (info.message.authorId != user.id) {
                            throw HttpException(HttpStatusCode.Forbidden)
                        }
                    }
            }.map(SentMessageInfoDtoFactory::create).map(::GetSentMessageResponseDto).single()

    override suspend fun deleteSentMessage(
        authToken: Token,
        messageId: Identifier,
    ) = fetchUserByAccessTokenFunction.fetchUser(authToken).requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getMessage(messageId).orNotFoundError().requireAuthor(user)
        }.flatMapLatest {
            messagesRepository.removeMessage(
                messageId = messageId,
            )
        }.single()

    override suspend fun getReceivedMessages(
        authToken: Token,
        range: RangeDto,
    ): GetReceivedMessagesResponseDto =
        fetchUserByAccessTokenFunction.fetchUser(authToken).requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.getMessagesByAddresseeUserId(
                    userId = user.id,
                    range = RangeDtoConverter.convert(range),
                )
                    .map { messages ->
                        coroutineScope {
                            messages.map { message ->
                                async { getReceivedMessageFunction.get(message, user) }
                            }.awaitAll()
                        }
                    }
            }
            .map(::GetReceivedMessagesResponseDto).single()

    override suspend fun getReceivedMessage(
        authToken: Token,
        messageId: Identifier,
    ): GetReceivedMessageDto =
        fetchUserByAccessTokenFunction.fetchUser(authToken).requireAuthorizedUser()
            .flatMapLatest { user ->
                messagesRepository.getMessage(messageId).orNotFoundError().requireAddressee(user)
                    .map { message -> getReceivedMessageFunction.get(message, user) }
            }.map(::GetReceivedMessageDto).single()

    override suspend fun setReceivedMessageAnswer(
        authToken: Token,
        messageId: Identifier,
        request: UpdateReceivedMessageAnswerRequestDto,
    ): Unit = fetchUserByAccessTokenFunction.fetchUser(authToken).requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getMessage(messageId).orNotFoundError().requireAddressee(user)
                .flatMapLatest {
                    messagesRepository.setReceivedMessageAnswer(
                        messageId = messageId,
                        userId = user.id,
                        answerIds = request.answerIds,
                    )
                }
                .orNotFoundError()
        }.collect()

    override suspend fun setReceivedMessageNotify(
        authToken: Token,
        messageId: Identifier,
    ): Unit = fetchUserByAccessTokenFunction.fetchUser(authToken).requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getMessage(messageId).orNotFoundError().requireAddressee(user)
                .flatMapLatest {
                    messagesRepository.setReceivedMessageNotify(
                        messageId = messageId,
                        userId = user.id,
                    )
                }.orNotFoundError()
        }.collect()

    override suspend fun setReadMessageNotify(
        authToken: Token,
        messageId: Identifier,
    ): Unit = fetchUserByAccessTokenFunction.fetchUser(authToken).requireAuthorizedUser()
        .flatMapLatest { user ->
            messagesRepository.getMessage(messageId).orNotFoundError().requireAddressee(user)
                .flatMapLatest {
                    messagesRepository.setReadMessageNotify(
                        messageId = messageId,
                        userId = user.id,
                    )
                }.orNotFoundError()
        }.collect()

}