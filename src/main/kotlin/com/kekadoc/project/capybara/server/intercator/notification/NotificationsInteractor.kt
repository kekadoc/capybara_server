package com.kekadoc.project.capybara.server.intercator.notification

import com.kekadoc.project.capybara.server.routing.api.notifications.model.*

interface NotificationsInteractor {

    suspend fun getSentNotifications(
        authToken: String,
    ): GetSentNotificationsResponse

    suspend fun createSentNotification(
        authToken: String,
        request: CreateSentNotificationRequest,
    ): CreateSentNotificationResponse

    suspend fun getSentNotification(
        authToken: String,
        messageId: String,
    ): GetSentNotificationResponse

    suspend fun updateSentNotification(
        authToken: String,
        messageId: String,
        request: UpdateSentMessageRequest,
    ): UpdateSentNotificationResponse

    suspend fun deleteSentNotification(
        authToken: String,
        messageId: String,
    )

    suspend fun getReceivedNotifications(
        authToken: String,
    ): GetReceivedNotifications

    suspend fun getReceivedNotification(
        authToken: String,
        messageId: String,
    ): GetReceivedNotification

    suspend fun setReceivedNotificationAnswer(
        authToken: String,
        messageId: String,
        request: PostReceivedMessageAnswerRequest,
    )

    suspend fun setReceivedNotificationNotify(
        authToken: String,
        messageId: String,
        request: PostReceivedMessageNotifyRequest,
    )

}