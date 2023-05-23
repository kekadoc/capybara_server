package com.kekadoc.project.capybara.server.data.repository.notification.mobile

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.Token
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

    fun sendNotification(
        userId: Identifier,
        pushToken: String,
        message: Message,
    ): Flow<Unit>

}