package com.kekadoc.project.capybara.server.data.source.api.notification.mobile.local

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Token

interface MobileNotificationsLocalDataSource {

    suspend fun setUserPushToken(
        userId: Identifier,
        pushToken: Token,
    )

    suspend fun getPushTokens(
        userIds: List<Identifier>,
    ): Map<Identifier, String?>

    suspend fun getUserPushToken(
        userId: Identifier,
    ): String?

    suspend fun deleteUserPushToken(
        userId: Identifier,
    )

    suspend fun savePushNotificationId(
        userId: Identifier,
        notificationId: Identifier,
        pushId: String,
    )

}