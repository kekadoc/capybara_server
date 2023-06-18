package com.kekadoc.project.capybara.server.data.repository.notification.mobile

import com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.local.MobileNotificationsLocalDataSource
import com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.remote.MobileNotificationsRemoteDataSource
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.message.MessageAction
import com.kekadoc.project.capybara.server.domain.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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
        message: Message,
        author: User,
        actions: List<MessageAction>,
    ): Flow<Unit> = flowOf {
        val pushId = remoteDataSource.sendNotification(
            pushToken = pushToken,
            message = message,
            author = author,
        )
        localDataSource.savePushNotificationId(
            userId = userId,
            messageId = message.id,
            pushId = pushId,
        )
    }

}