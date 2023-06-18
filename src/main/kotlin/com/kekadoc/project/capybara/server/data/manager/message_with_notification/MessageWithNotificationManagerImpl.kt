package com.kekadoc.project.capybara.server.data.manager.message_with_notification

import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.notification.email.EmailNotificationRepository
import com.kekadoc.project.capybara.server.data.repository.notification.mobile.MobileNotificationsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.domain.model.*
import com.kekadoc.project.capybara.server.domain.model.group.Group
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.message.MessageAction
import com.kekadoc.project.capybara.server.domain.model.message.MessageNotifications
import com.kekadoc.project.capybara.server.domain.model.message.MessageType
import com.kekadoc.project.capybara.server.domain.model.user.User
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
        author: User,
        type: MessageType,
        title: String?,
        text: String,
        actions: List<MessageAction>?,
        isMultiAction: Boolean,
        addresseeUsers: List<Identifier>,
        addresseeGroups: Map<Identifier, List<Identifier>?>,
        notifications: MessageNotifications?
    ): Flow<Message> = messagesRepository.createMessage(
        authorId = author.id,
        type = type,
        title = title,
        text = text,
        actions = actions,
        isMultiAction = isMultiAction,
        addresseeUsers = addresseeUsers,
        addresseeGroups = addresseeGroups,
        notifications = notifications,
    )
        .onEach { message ->
            handleMessageByNotifications(
                message = message,
                author = author,
            )
        }

    private suspend fun handleMessageByNotifications(
        message: Message,
        author: User,
    ) = coroutineScope {
        val allUserIds = getAllUsersForMessage(message)
        println("handleMessageByNotifications $allUserIds ${message.notifications}")
        with(message.notifications) {
            if (this.email) sentEmailNotification(message, author, allUserIds)
            if (this.app) sentAppNotification(message, author, allUserIds)
            if (this.messengers) { Unit } // TODO: Messengers notifications
        }
    }

    private fun CoroutineScope.sentEmailNotification(
        message: Message,
        author: User,
        allUserIds: List<Identifier>,
    ) = launch(Dispatchers.IO) {
        val users = usersRepository.getUsersByIds(allUserIds).single()
        emailNotificationRepository.sentEmailNotification(message, author, users).collect()
    }

    private fun CoroutineScope.sentAppNotification(
        message: Message,
        author: User,
        allUserIds: List<Identifier>,
    ) = launch(Dispatchers.IO) {
        println("PUSH TOKENS $allUserIds")
        mobileNotificationsRepository.getPushTokens(allUserIds)
            .onEach { userIdsWithTokens ->
                println("PUSH TOKENS $userIdsWithTokens")
                coroutineScope {
                    userIdsWithTokens.forEach { (userId, token) ->
                        if (token != null) {
                            launch(Dispatchers.IO) {
                                mobileNotificationsRepository.sendNotification(
                                    userId = userId,
                                    pushToken = token,
                                    message = message,
                                    author = author,
                                    actions = message.actions,
                                ).collect()
                            }
                        }
                    }
                }
            }
            .collect()
    }

    private suspend fun getAllUsersForMessage(message: Message): List<Identifier> {
        println("______getAllUsersForMessage=$message")
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