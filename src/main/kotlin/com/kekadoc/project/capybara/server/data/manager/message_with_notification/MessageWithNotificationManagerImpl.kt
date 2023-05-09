package com.kekadoc.project.capybara.server.data.manager.message_with_notification

import com.kekadoc.project.capybara.server.common.extensions.mapElements
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.notification.email.EmailNotificationRepository
import com.kekadoc.project.capybara.server.data.repository.notification.mobile.MobileNotificationsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.domain.model.Group
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Message
import com.kekadoc.project.capybara.server.domain.model.User
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
        type: Message.Type,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: Message.Content,
        actions: Message.Actions?,
        notifications: Message.Notifications,
    ): Flow<Message> = messagesRepository.createMessage(
        authorId = authorId,
        type = type,
        addresseeGroups = addresseeGroups,
        addresseeUsers = addresseeUsers,
        content = content,
        notifications = notifications,
        actions = actions,
    )
        .onEach(::handleMessageByNotifications)

    private suspend fun handleMessageByNotifications(message: Message) = coroutineScope {
        val allUserIds = getAllUsersForMessage(message)
        println("__TEST__handleMessageByNotifications__message=$message")
        println("__TEST__handleMessageByNotifications__allUserIds=$allUserIds")
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