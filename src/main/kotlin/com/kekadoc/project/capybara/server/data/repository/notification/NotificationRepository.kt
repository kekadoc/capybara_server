package com.kekadoc.project.capybara.server.data.repository.notification

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.model.NotificationInfo
import com.kekadoc.project.capybara.server.routing.api.notifications.model.PostReceivedMessageAnswerRequest
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun createNotification(
        authorId: Identifier,
        type: Notification.Type,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: Notification.Content,
    ): Flow<Notification>

    fun updateNotification(
        messageId: Identifier,
        content: Notification.Content,
    ): Flow<Notification?>

    fun updateNotificationStatus(
        messageId: Identifier,
        status: NotificationInfo.Status,
    ): Flow<Notification?>

    fun updateNotificationUserInfo(
        messageId: Identifier,
        info: NotificationInfo.FromUserInfo,
    ): Flow<Notification?>

    fun removeNotification(messageId: Identifier): Flow<Unit>
    
    fun getNotification(messageId: Identifier): Flow<Notification?>

    fun getNotificationInfo(messageId: Identifier): Flow<NotificationInfo?>

    fun getNotificationsByAuthorId(authorId: Identifier): Flow<List<Notification>>

    fun getNotificationsByAddresseeUserId(userId: Identifier): Flow<List<Notification>>

    fun getNotificationsByAddresseeGroupIds(groupIds: Set<Identifier>): Flow<List<Notification>>

    fun setReceivedNotificationAnswer(
        notificationId: Identifier,
        userId: Identifier,
        request: PostReceivedMessageAnswerRequest,
    ): Flow<Notification?>

    fun setReceivedNotificationNotify(
        notificationId: Identifier,
        userId: Identifier,
    ): Flow<Notification?>

    fun setReadNotificationNotify(
        notificationId: Identifier,
        userId: Identifier,
    ): Flow<Notification?>

}