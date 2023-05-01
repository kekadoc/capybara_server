package com.kekadoc.project.capybara.server.data.source.api.notification

import com.kekadoc.project.capybara.server.common.exception.GroupNotFound
import com.kekadoc.project.capybara.server.common.exception.NotificationNotFound
import com.kekadoc.project.capybara.server.common.exception.UserNotFound
import com.kekadoc.project.capybara.server.common.extensions.orElse
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.model.NotificationInfo
import com.kekadoc.project.capybara.server.data.source.database.entity.*
import com.kekadoc.project.capybara.server.data.source.database.table.NotificationsForGroupsTable
import com.kekadoc.project.capybara.server.data.source.database.table.NotificationsForUsersTable
import com.kekadoc.project.capybara.server.data.source.database.table.NotificationsTable
import com.kekadoc.project.capybara.server.data.source.factory.dto.NotificationFactory
import com.kekadoc.project.capybara.server.routing.api.notifications.model.PostReceivedMessageAnswerRequest
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

class NotificationsDataSourceImpl : NotificationsDataSource {

    override suspend fun createNotification(
        authorId: Identifier,
        type: Notification.Type,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: Notification.Content,
    ): Notification = transaction {
        val userEntity = UserEntity.findById(authorId).orElse { throw UserNotFound(authorId) }

        val notificationEntity = NotificationEntity.new {
            this.author = userEntity
            this.type = type.name;
            this.contentTitle = content.title
            this.contentText = content.text
            this.contentImage = content.image
            this.status = NotificationInfo.Status.RECEIVED.toString()
        }

        addresseeGroups.forEach { groupId ->
            val group = GroupEntity.findById(groupId) ?: throw GroupNotFound(groupId)
            NotificationsForGroupEntity.new {
                this.notificationId = notificationEntity
                this.group = group
            }
            group.members.forEach { member ->
                NotificationsForUserEntity.new {
                    this.notificationId = notificationEntity
                    this.userId = member.user
                }
            }
        }

        addresseeUsers.forEach { userId ->
            val user = UserEntity.findById(userId) ?: throw UserNotFound(userId)
            NotificationsForUserEntity.new {
                this.notificationId = notificationEntity
                this.userId = user
            }
        }

        NotificationFactory.create(notificationEntity)
    }

    override suspend fun updateNotification(
        notificationId: Identifier,
        content: Notification.Content,
    ): Notification? = transaction {
        NotificationEntity.findById(notificationId)
            ?.apply {
                this.contentTitle = content.title
                this.contentText = content.text
                this.contentImage = content.image
            }
            ?.let(NotificationFactory::create)
    }

    override suspend fun updateNotificationStatus(
        notificationId: Identifier,
        status: NotificationInfo.Status,
    ): Notification? = transaction {
        NotificationEntity.findById(notificationId)
            ?.apply {
                this.status = status.name
            }
            ?.let(NotificationFactory::create)
    }

    override suspend fun updateNotificationUserInfo(
        notificationId: Identifier,
        info: NotificationInfo.FromUserInfo,
    ): Notification? = transaction {
        val user = UserEntity.findById(info.userId) ?: throw UserNotFound(info.userId)
        val notification = NotificationEntity.findById(notificationId)
            ?: throw NotificationNotFound(notificationId)
        val all = NotificationsForUserEntity.find {
            NotificationsForUsersTable.userId eq info.userId
        }

        val entity = if (all.empty()) {
            NotificationsForUserEntity.new {
                this.notificationId = notification
                this.userId = user
                this.received = info.received
                this.read = info.read
                this.answer = info.answer
            }
        } else {
            all.first().apply {
                this.received = info.received
                this.read = info.read
                this.answer = info.answer
            }
        }

        NotificationFactory.create(entity.notificationId)
    }

    override suspend fun removeNotification(
        notificationId: Identifier,
    ): Notification? = transaction {
        NotificationEntity.findById(notificationId)
            ?.apply { delete() }
            ?.let(NotificationFactory::create)
    }

    override suspend fun getNotification(
        notificationId: Identifier,
    ): Notification? = transaction {
        NotificationEntity.findById(notificationId)
            ?.let(NotificationFactory::create)
    }

    override suspend fun getNotificationInfo(
        notificationId: Identifier,
    ): NotificationInfo? = transaction {
        val notificationEntity = NotificationEntity.findById(notificationId)
            .orElse { throw NotificationNotFound(notificationId) }
        val addresseeUsers = notificationEntity.addresseeUsers
            .filter { !it.fromGroup }
            .map {
                NotificationInfo.FromUserInfo(
                    userId = it.userId.id.value,
                    received = it.received,
                    read = it.read,
                    answer = it.answer,
                )
            }
        val addresseeGroups = notificationEntity.addresseeGroups.map { forGroup ->
            val membersIds = forGroup.group.members.map { member -> member.user.id.value }
            val members: List<NotificationInfo.FromUserInfo> = NotificationsForUserEntity.find {
                (NotificationsForUsersTable.notificationId eq notificationId) and (NotificationsForUsersTable.userId inList membersIds) and (NotificationsForUsersTable.fromGroup eq true)
            }.map { forUser ->
                NotificationInfo.FromUserInfo(
                    userId = forUser.userId.id.value,
                    received = forUser.received,
                    read = forUser.read,
                    answer = forUser.answer,
                )
            }
            NotificationInfo.GroupInfo(
                id = forGroup.group.id.value,
                name = forGroup.group.name,
                members = members,
            )
        }
        NotificationInfo(
            notification = NotificationFactory.create(notificationEntity),
            addresseeGroups = addresseeGroups,
            addresseeUsers = addresseeUsers,
            status = NotificationInfo.Status.values()
                .find { it.name == notificationEntity.status }
                .orElse { NotificationInfo.Status.UNDEFINED },
        )
    }

    override suspend fun getNotificationsByAuthorId(
        authorId: Identifier,
    ): List<Notification> = transaction {
        NotificationEntity.find { NotificationsTable.author eq authorId }
            .map(NotificationFactory::create)
    }

    override suspend fun getNotificationByAddresseeUserId(
        userId: Identifier,
    ): List<Notification> = transaction {
        NotificationsForUserEntity
            .find { NotificationsForUsersTable.userId eq userId }
            .map(NotificationsForUserEntity::notificationId)
            .map(NotificationFactory::create)
    }

    override suspend fun getNotificationByAddresseeGroupIds(
        groupIds: Set<Identifier>,
    ): List<Notification> = transaction {
        NotificationsForGroupEntity
            .find { NotificationsForGroupsTable.group inList groupIds }
            .map(NotificationsForGroupEntity::notificationId)
            .map(NotificationFactory::create)
    }

    override suspend fun setReceivedNotificationAnswer(
        notificationId: Identifier,
        userId: Identifier,
        request: PostReceivedMessageAnswerRequest,
    ): Notification? = transaction {
        NotificationsForUserEntity.find {
            (NotificationsForUsersTable.userId eq userId) and (NotificationsForUsersTable.notificationId eq notificationId)
        }
            .map { entity -> entity.apply { this.answer = request.answer } }
            .firstOrNull()
            ?.let(NotificationsForUserEntity::notificationId)
            ?.let(NotificationFactory::create)
    }

    override suspend fun setReceivedNotificationNotify(
        notificationId: Identifier,
        userId: Identifier,
    ): Notification? = transaction {
        NotificationsForGroupEntity
        NotificationsForUserEntity.find {
            (NotificationsForUsersTable.userId eq userId) and (NotificationsForUsersTable.notificationId eq notificationId)
        }
            .map { entity -> entity.apply { this.received = true } }
            .firstOrNull()
            ?.let(NotificationsForUserEntity::notificationId)
            ?.let(NotificationFactory::create)
    }

    override suspend fun setReadNotificationNotify(
        notificationId: Identifier,
        userId: Identifier,
    ): Notification? = transaction {
        NotificationsForUserEntity.find {
            (NotificationsForUsersTable.userId eq userId) and (NotificationsForUsersTable.notificationId eq notificationId)
        }
            .map { entity -> entity.apply { this.read = true } }
            .firstOrNull()
            ?.let(NotificationsForUserEntity::notificationId)
            ?.let(NotificationFactory::create)
    }

}