package com.kekadoc.project.capybara.server.data.repository.notification

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.model.NotificationInfo
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun getAll(): Flow<List<Notification>>
    
    fun createNotification(
        authorId: Identifier,
        type: Notification.Type,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: Notification.Content,
    ): Flow<Notification>

    fun updateMessage(
        messageId: Identifier,
        content: Notification.Content,
    ): Flow<Notification>

    fun updateMessageStatus(
        messageId: Identifier,
        status: NotificationInfo.Status,
    ): Flow<Notification>

    fun updateMessageUserInfo(
        messageId: Identifier,
        info: NotificationInfo.FromUserInfo,
    ): Flow<Notification>

    fun removeMessage(messageId: Identifier): Flow<Unit>
    
    fun getMessage(messageId: Identifier): Flow<Notification?>
    
    fun getMessagesByAuthorId(authorId: Identifier): Flow<List<Notification>>

    fun getMessagesByAddresseeUserId(userId: Identifier): Flow<List<Notification>>

    fun getMessagesByAddresseeGroupIds(groupIds: Set<Identifier>): Flow<List<Notification>>
    
}