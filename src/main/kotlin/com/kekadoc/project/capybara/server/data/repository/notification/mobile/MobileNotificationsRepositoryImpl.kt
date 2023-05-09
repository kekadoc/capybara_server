package com.kekadoc.project.capybara.server.data.repository.notification.mobile

import com.kekadoc.project.capybara.server.common.extensions.flowOf
import com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.local.MobileNotificationsLocalDataSource
import com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.remote.MobileNotificationsRemoteDataSource
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Message
import com.kekadoc.project.capybara.server.domain.model.Token
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

    override fun sendNotification(
        userId: Identifier,
        pushToken: String,
        message: Message
    ): Flow<Unit> = flowOf {
        val pushId = remoteDataSource.sendNotification(
            pushToken = pushToken,
            title = message.content.title,
            body = message.content.text,
            imageUrl = message.content.image,
        )
        localDataSource.savePushNotificationId(
            userId = userId,
            messageId = message.id,
            pushId = pushId,
        )
    }

}