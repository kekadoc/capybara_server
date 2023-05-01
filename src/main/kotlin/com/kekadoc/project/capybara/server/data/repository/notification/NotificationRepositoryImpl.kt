package com.kekadoc.project.capybara.server.data.repository.notification

import com.kekadoc.project.capybara.server.common.extensions.flowOf
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.model.NotificationInfo
import com.kekadoc.project.capybara.server.data.repository.notification.mobile.MobileNotificationsRepository
import com.kekadoc.project.capybara.server.data.source.api.notification.NotificationsDataSource
import com.kekadoc.project.capybara.server.routing.api.notifications.model.PostReceivedMessageAnswerRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class NotificationRepositoryImpl(
    private val notificationsDataSource: NotificationsDataSource,
) : NotificationRepository {

    override fun createNotification(
        authorId: Identifier,
        type: Notification.Type,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: Notification.Content,
    ): Flow<Notification> = flowOf {
        notificationsDataSource.createNotification(
            authorId = authorId,
            type = type,
            addresseeGroups = addresseeGroups,
            addresseeUsers = addresseeUsers,
            content = content,
        )
    }

    override fun updateNotification(
        messageId: Identifier,
        content: Notification.Content,
    ): Flow<Notification?> = flowOf {
        notificationsDataSource.updateNotification(
            notificationId = messageId,
            content = content,
        )
    }

    override fun updateNotificationStatus(
        messageId: Identifier,
        status: NotificationInfo.Status,
    ): Flow<Notification?> = flowOf {
        notificationsDataSource.updateNotificationStatus(
            notificationId = messageId,
            status = status,
        )
    }

    override fun updateNotificationUserInfo(
        messageId: Identifier,
        info: NotificationInfo.FromUserInfo,
    ): Flow<Notification?> = flowOf {
        notificationsDataSource.updateNotificationUserInfo(
            notificationId = messageId,
            info = info,
        )
    }

    override fun removeNotification(
        messageId: Identifier,
    ): Flow<Unit> = flowOf {
        notificationsDataSource.removeNotification(
            notificationId = messageId,
        )
    }

    override fun getNotification(
        messageId: Identifier,
    ): Flow<Notification?> = flowOf {
        notificationsDataSource.getNotification(
            notificationId = messageId,
        )
    }

    override fun getNotificationInfo(messageId: Identifier): Flow<NotificationInfo?> = flowOf {
        notificationsDataSource.getNotificationInfo(
            notificationId = messageId,
        )
    }

    override fun getNotificationsByAuthorId(
        authorId: Identifier,
    ): Flow<List<Notification>> = flowOf {
        notificationsDataSource.getNotificationsByAuthorId(
            authorId = authorId,
        )
    }

    override fun getNotificationsByAddresseeUserId(
        userId: Identifier,
    ): Flow<List<Notification>> = flowOf {
        notificationsDataSource.getNotificationByAddresseeUserId(
            userId = userId,
        )
    }

    override fun getNotificationsByAddresseeGroupIds(
        groupIds: Set<Identifier>,
    ): Flow<List<Notification>> = flowOf {
        notificationsDataSource.getNotificationByAddresseeGroupIds(
            groupIds = groupIds,
        )
    }

    override fun setReceivedNotificationAnswer(
        notificationId: Identifier,
        userId: Identifier,
        request: PostReceivedMessageAnswerRequest,
    ): Flow<Notification?> = flowOf {
        notificationsDataSource.setReceivedNotificationAnswer(
            notificationId = notificationId,
            userId = userId,
            request = request,
        )
    }

    override fun setReceivedNotificationNotify(
        notificationId: Identifier,
        userId: Identifier,
    ): Flow<Notification?> = flowOf {
        notificationsDataSource.setReceivedNotificationNotify(
            notificationId = notificationId,
            userId = userId,
        )
    }

    override fun setReadNotificationNotify(
        notificationId: Identifier,
        userId: Identifier,
    ): Flow<Notification?> = flowOf {
        notificationsDataSource.setReadNotificationNotify(
            notificationId = notificationId,
            userId = userId,
        )
    }

}