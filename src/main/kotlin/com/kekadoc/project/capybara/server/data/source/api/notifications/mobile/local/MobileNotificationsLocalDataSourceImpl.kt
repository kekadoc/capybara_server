package com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.local

import com.kekadoc.project.capybara.server.common.exception.MessageNotFound
import com.kekadoc.project.capybara.server.common.exception.UserNotFound
import com.kekadoc.project.capybara.server.common.extensions.orElse
import com.kekadoc.project.capybara.server.data.source.database.entity.MessageEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.MobilePushNotificationEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.PushTokenEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserEntity
import com.kekadoc.project.capybara.server.data.source.database.table.PushTokensTable
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

class MobileNotificationsLocalDataSourceImpl : MobileNotificationsLocalDataSource {

    override suspend fun setUserPushToken(
        userId: Identifier,
        pushToken: Token,
    ): Unit = transaction {
        PushTokensTable.deleteWhere { user eq userId }
        PushTokenEntity.new {
            this.user = UserEntity.findById(userId) ?: throw UserNotFound(userId)
            this.token = pushToken
        }
    }

    override suspend fun getPushTokens(
        userIds: List<Identifier>,
    ): Map<Identifier, String?> = transaction {
        val pushTokenEntities =
            PushTokenEntity.find { PushTokensTable.user inList userIds }.toList()
        userIds.associateWith { userId -> pushTokenEntities.find { it.user.id.value == userId }?.token }
    }

    override suspend fun getUserPushToken(
        userId: Identifier,
    ): String? = transaction {
        PushTokenEntity.find { PushTokensTable.user eq userId }.firstOrNull()?.token
    }

    override suspend fun deleteUserPushToken(
        userId: Identifier,
    ): Unit = transaction {
        PushTokensTable.deleteWhere { user eq userId }
    }

    override suspend fun savePushNotificationId(
        userId: Identifier,
        messageId: Identifier,
        pushId: String,
    ): Unit = transaction {
        MobilePushNotificationEntity.new {
            this.user = UserEntity.findById(userId) ?: throw UserNotFound(userId)
            this.notification = MessageEntity.findById(messageId)
                .orElse { throw MessageNotFound(messageId) }
            this.pushId = pushId
        }
    }

}