package com.kekadoc.project.capybara.server.data.repository.notification.mobile

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Token
import kotlinx.coroutines.flow.Flow

interface MobileNotificationsRepository {

    fun setPushToken(
        userId: Identifier,
        pushToken: Token,
    ): Flow<Unit>

    fun getPushToken(
        userId: Identifier,
    ): Flow<String?>

    fun getPushTokens(
        userIds: List<Identifier>,
    ): Flow<Map<Identifier, String?>>

    fun deletePushToken(
        userId: Identifier,
    ): Flow<Unit>

    fun savePushNotificationId(
        userId: Identifier,
        notificationId: Identifier,
        pushId: String,
    ): Flow<Unit>

    fun sendNotification(
        pushToken: String,
        title: String?,
        body: String,
        imageUrl: String?,
    ): Flow<String>

}