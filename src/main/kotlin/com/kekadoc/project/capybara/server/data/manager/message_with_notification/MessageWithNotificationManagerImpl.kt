package com.kekadoc.project.capybara.server.data.manager.message_with_notification

import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.notification.email.EmailNotificationRepository
import com.kekadoc.project.capybara.server.data.repository.notification.mobile.MobileNotificationsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.domain.model.*
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.message.MessageNotifications
import com.kekadoc.project.capybara.server.domain.model.message.MessageType
import com.kekadoc.project.capybara.server.domain.model.message.MessageAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MessageWithNotificationManagerImpl(
    private val usersRepository: UsersRepository,
    private val groupsRepository: GroupsRepository,
    private val messagesRepository: MessagesRepository,
    private val emailNotificationRepository: EmailNotificationRepository,
    private val mobileNotificationsRepository: MobileNotificationsRepository,
) : MessageWithNotificationManager {

    override fun sentMessage(
        authorId: Identifier,
        type: MessageType,
        title: String?,
        text: String,
        actions: List<MessageAction>?,
        isMultiAction: Boolean,
        addresseeUsers: List<Identifier>,
        addresseeGroups: List<Identifier>,
        notifications: MessageNotifications?
    ): Flow<Message> = messagesRepository.createMessage(
        authorId = authorId,
        type = type,
        title = title,
        text = text,
        actions = actions,
        isMultiAction = isMultiAction,
        addresseeUsers = addresseeUsers,
        addresseeGroups = addresseeGroups,
        notifications = notifications,
    )
        .onEach(::handleMessageByNotifications)

    private suspend fun handleMessageByNotifications(message: Message) = coroutineScope {
        val allUserIds = getAllUsersForMessage(message)
        with(message.notifications) {
            if (this.email) sentEmailNotification(message, allUserIds)
            if (this.app) sentAppNotification(message, allUserIds)
            if (this.sms) { Unit } // TODO: Sms notifications
            if (this.messengers) { Unit } // TODO: Messengers notifications
        }
    }

    private fun CoroutineScope.sentEmailNotification(
        message: Message,
        allUserIds: List<Identifier>,
    ) = launch(Dispatchers.IO) {
        val users = usersRepository.getUsersByIds(allUserIds).single()
        emailNotificationRepository.sentEmailNotification(message, users).collect()
    }

    private fun CoroutineScope.sentAppNotification(
        message: Message,
        allUserIds: List<Identifier>,
    ) = launch(Dispatchers.IO) {
        mobileNotificationsRepository.getPushTokens(allUserIds)
            .onEach { userIdsWithTokens ->
                coroutineScope {
                    userIdsWithTokens.forEach { (userId, token) ->
                        if (token != null) {
                            launch(Dispatchers.IO) {
                                mobileNotificationsRepository.sendNotification(
                                    userId = userId,
                                    pushToken = token,
                                    message = message,
                                )
                            }
                        }
                    }
                }
            }
    }

    private suspend fun getAllUsersForMessage(message: Message): List<Identifier> {
        val usersFromGroupsFlow = groupsRepository.getGroups(message.addresseeGroupIds)
            .mapElements(Group::members)
            .map(List<List<User>>::flatten)
            .mapElements(User::id)
            .map(List<Identifier>::distinct)

        val usersFlow = usersRepository.getUsersByIds(message.addresseeUserIds)
            .mapElements(User::id)
            .map(List<Identifier>::distinct)

        return combine(
            usersFromGroupsFlow,
            usersFlow
        ) { usersFromGroups, users ->
            (usersFromGroups + users).distinct()
        }.single()
    }

}