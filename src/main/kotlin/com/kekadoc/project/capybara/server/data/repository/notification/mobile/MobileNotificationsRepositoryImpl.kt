package com.kekadoc.project.capybara.server.data.repository.notification.mobile

import com.kekadoc.project.capybara.server.common.extensions.flowOf
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Token
import com.kekadoc.project.capybara.server.data.source.api.notification.mobile.local.MobileNotificationsLocalDataSource
import com.kekadoc.project.capybara.server.data.source.api.notification.mobile.remote.MobileNotificationsRemoteDataSource
import kotlinx.coroutines.flow.Flow

class MobileNotificationsRepositoryImpl(
    private val localDataSource: MobileNotificationsLocalDataSource,
    private val remoteDataSource: MobileNotificationsRemoteDataSource,
) : MobileNotificationsRepository {

    override fun setPushToken(userId: Identifier, pushToken: Token): Flow<Unit> = flowOf {
        localDataSource.setUserPushToken(userId, pushToken)
    }

    override fun getPushToken(userId: Identifier): Flow<String?> = flowOf {
        localDataSource.getUserPushToken(userId)
    }

    override fun getPushTokens(userIds: List<Identifier>): Flow<Map<Identifier, String?>> = flowOf {
        localDataSource.getPushTokens(userIds)
    }

    override fun deletePushToken(userId: Identifier): Flow<Unit> = flowOf {
        localDataSource.deleteUserPushToken(userId)
    }

    override fun savePushNotificationId(
        userId: Identifier,
        notificationId: Identifier,
        pushId: String,
    ): Flow<Unit> = flowOf {
        localDataSource.savePushNotificationId(
            userId = userId,
            notificationId = notificationId,
            pushId = pushId
        )
    }

    override fun sendNotification(
        pushToken: String,
        title: String?,
        body: String,
        imageUrl: String?,
    ): Flow<String> = flowOf {
        remoteDataSource.sendNotification(pushToken, title, body, imageUrl)
    }

}