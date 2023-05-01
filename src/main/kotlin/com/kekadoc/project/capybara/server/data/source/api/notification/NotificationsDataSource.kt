package com.kekadoc.project.capybara.server.data.source.api.notification

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.model.NotificationInfo
import com.kekadoc.project.capybara.server.routing.api.notifications.model.PostReceivedMessageAnswerRequest

interface NotificationsDataSource {

    suspend fun createNotification(
        authorId: Identifier,
        type: Notification.Type,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: Notification.Content,
    ): Notification

    suspend fun updateNotification(
        notificationId: Identifier,
        content: Notification.Content,
    ): Notification?

    suspend fun updateNotificationStatus(
        notificationId: Identifier,
        status: NotificationInfo.Status,
    ): Notification?

    suspend fun updateNotificationUserInfo(
        notificationId: Identifier,
        info: NotificationInfo.FromUserInfo,
    ): Notification?

    suspend fun removeNotification(notificationId: Identifier): Notification?

    suspend fun getNotification(notificationId: Identifier): Notification?

    suspend fun getNotificationInfo(notificationId: Identifier): NotificationInfo?

    suspend fun getNotificationsByAuthorId(authorId: Identifier): List<Notification>

    suspend fun getNotificationByAddresseeUserId(userId: Identifier): List<Notification>

    suspend fun getNotificationByAddresseeGroupIds(groupIds: Set<Identifier>): List<Notification>

    suspend fun setReceivedNotificationAnswer(
        notificationId: Identifier,
        userId: Identifier,
        request: PostReceivedMessageAnswerRequest,
    ): Notification?

    suspend fun setReceivedNotificationNotify(
        notificationId: Identifier,
        userId: Identifier,
    ): Notification?

    suspend fun setReadNotificationNotify(
        notificationId: Identifier,
        userId: Identifier,
    ): Notification?
}