package com.kekadoc.project.capybara.server.intercator.notification

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Token
import com.kekadoc.project.capybara.server.routing.api.notifications.model.*

interface NotificationsInteractor {

    suspend fun getSentNotifications(
        authToken: Token,
    ): GetSentNotificationsResponse

    suspend fun createSentNotification(
        authToken: Token,
        request: CreateSentNotificationRequest,
    ): CreateSentNotificationResponse

    suspend fun getSentNotification(
        authToken: Token,
        notificationId: Identifier,
    ): GetSentNotificationResponse

    suspend fun updateSentNotification(
        authToken: Token,
        notificationId: Identifier,
        request: UpdateSentMessageRequest,
    ): UpdateSentNotificationResponse

    suspend fun deleteSentNotification(
        authToken: Token,
        notificationId: Identifier,
    )

    suspend fun getReceivedNotifications(
        authToken: Token,
    ): GetReceivedNotifications

    suspend fun getReceivedNotification(
        authToken: Token,
        notificationId: Identifier,
    ): GetReceivedNotification

    suspend fun setReceivedNotificationAnswer(
        authToken: Token,
        notificationId: Identifier,
        request: PostReceivedMessageAnswerRequest,
    )

    suspend fun setReceivedNotificationNotify(
        authToken: Token,
        notificationId: Identifier,
    )

    suspend fun setReadNotificationNotify(
        authToken: Token,
        notificationId: Identifier,
    )

}