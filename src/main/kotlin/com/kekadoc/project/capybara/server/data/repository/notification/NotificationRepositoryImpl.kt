package com.kekadoc.project.capybara.server.data.repository.notification

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.model.NotificationInfo
import com.kekadoc.project.capybara.server.data.source.api.notification.NotificationsDataSource
import kotlinx.coroutines.flow.Flow

class NotificationRepositoryImpl(
    private val notificationsDataSource: NotificationsDataSource,
) : NotificationRepository {

    override fun getAll(): Flow<List<Notification>> {
        return notificationsDataSource.getAll()
    }

    override fun createNotification(
        authorId: Identifier,
        type: Notification.Type,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: Notification.Content,
    ): Flow<Notification> {
        return notificationsDataSource.createNotification(
            authorId = authorId,
            type = type,
            addresseeGroups = addresseeGroups,
            addresseeUsers = addresseeUsers,
            content = content,
        )
    }

    override fun updateMessage(
        messageId: Identifier,
        content: Notification.Content,
    ): Flow<Notification> {
        return notificationsDataSource.updateNotification(
            messageId = messageId,
            content = content,
        )
    }

    override fun updateMessageStatus(
        messageId: Identifier,
        status: NotificationInfo.Status,
    ): Flow<Notification> {
        return notificationsDataSource.updateNotificationStatus(
            messageId = messageId,
            status = status,
        )
    }

    override fun updateMessageUserInfo(
        messageId: Identifier,
        info: NotificationInfo.FromUserInfo,
    ): Flow<Notification> {
        return notificationsDataSource.updateNotificationUserInfo(
            messageId = messageId,
            info = info,
        )
    }

    override fun removeMessage(messageId: Identifier): Flow<Unit> {
        return notificationsDataSource.removeNotification(
            messageId = messageId,
        )
    }

    override fun getMessage(messageId: Identifier): Flow<Notification?> {
        return notificationsDataSource.getNotification(
            messageId = messageId,
        )
    }

    override fun getMessagesByAuthorId(authorId: Identifier): Flow<List<Notification>> {
        return notificationsDataSource.getNotificationsByAuthorId(
            authorId = authorId,
        )
    }

    override fun getMessagesByAddresseeUserId(userId: Identifier): Flow<List<Notification>> {
        return notificationsDataSource.getNotificationByAddresseeUserId(
            userId = userId,
        )
    }

    override fun getMessagesByAddresseeGroupIds(groupIds: Set<Identifier>): Flow<List<Notification>> {
        return notificationsDataSource.getNotificationByAddresseeGroupIds(
            groupIds = groupIds,
        )
    }


}