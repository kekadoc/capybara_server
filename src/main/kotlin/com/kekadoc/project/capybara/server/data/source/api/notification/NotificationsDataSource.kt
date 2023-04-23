package com.kekadoc.project.capybara.server.data.source.api.notification

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.model.NotificationInfo
import kotlinx.coroutines.flow.Flow

interface NotificationsDataSource {

    fun getAll(): Flow<List<Notification>>

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
    ): Flow<Notification>

    fun updateNotificationStatus(
        messageId: Identifier,
        status: NotificationInfo.Status,
    ): Flow<Notification>

    fun updateNotificationUserInfo(
        messageId: Identifier,
        info: NotificationInfo.FromUserInfo,
    ): Flow<Notification>

    fun removeNotification(messageId: Identifier): Flow<Unit>

    fun getNotification(messageId: Identifier): Flow<Notification?>

    fun getNotificationsByAuthorId(authorId: Identifier): Flow<List<Notification>>

    fun getNotificationByAddresseeUserId(userId: Identifier): Flow<List<Notification>>

    fun getNotificationByAddresseeGroupIds(groupIds: Set<Identifier>): Flow<List<Notification>>

}