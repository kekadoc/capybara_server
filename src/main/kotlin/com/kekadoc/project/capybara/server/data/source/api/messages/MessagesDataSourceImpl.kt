package com.kekadoc.project.capybara.server.data.source.api.messages

import com.kekadoc.project.capybara.server.common.exception.GroupNotFound
import com.kekadoc.project.capybara.server.common.exception.MessageNotFound
import com.kekadoc.project.capybara.server.common.exception.UserNotFound
import com.kekadoc.project.capybara.server.common.extensions.orElse
import com.kekadoc.project.capybara.server.data.source.database.entity.*
import com.kekadoc.project.capybara.server.data.source.database.entity.factory.MessageFactory
import com.kekadoc.project.capybara.server.data.source.database.table.MessageForGroupTable
import com.kekadoc.project.capybara.server.data.source.database.table.MessageForUserTable
import com.kekadoc.project.capybara.server.data.source.database.table.MessageTable
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Message
import com.kekadoc.project.capybara.server.domain.model.MessageInfo
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

class MessagesDataSourceImpl : MessagesDataSource {

    override suspend fun createMessage(
        authorId: Identifier,
        type: Message.Type,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: Message.Content,
        actions: Message.Actions?,
        notifications: Message.Notifications
    ): Message = transaction {
        val userEntity = UserEntity.findById(authorId) ?: throw UserNotFound(authorId)

        val messageEntity = MessageEntity.new {
            this.author = userEntity
            this.type = type.name
            this.contentTitle = content.title
            this.contentText = content.text
            this.contentImage = content.image
            this.status = MessageInfo.Status.RECEIVED.toString()
            this.notificationEmail = notifications.email
            this.notificationSms = notifications.sms
            this.notificationApp = notifications.app
            this.notificationMessengers = notifications.messengers
            this.action1 = actions?.action1
            this.action2 = actions?.action2
            this.action3 = actions?.action3
        }

        addresseeGroups.forEach { groupId ->
            val group = GroupEntity.findById(groupId) ?: throw GroupNotFound(groupId)
            MessageForGroupEntity.new {
                this.message = messageEntity
                this.group = group
            }
            group.members.forEach { member ->
                MessageForUserEntity.new {
                    this.message = messageEntity
                    this.userId = member.user
                }
            }
        }

        addresseeUsers.forEach { userId ->
            val user = UserEntity.findById(userId) ?: throw UserNotFound(userId)
            MessageForUserEntity.new {
                this.message = messageEntity
                this.userId = user
            }
        }

        MessageFactory.create(messageEntity)
    }

    override suspend fun updateMessage(
        messageId: Identifier,
        content: Message.Content,
    ): Message = transaction {
        MessageEntity.findById(messageId)
            ?.apply {
                this.contentTitle = content.title
                this.contentText = content.text
                this.contentImage = content.image
            }
            ?.let(MessageFactory::create)
            .orElse { throw MessageNotFound(messageId) }
    }

    override suspend fun updateMessageStatus(
        messageId: Identifier,
        status: MessageInfo.Status,
    ): Message = transaction {
        MessageEntity.findById(messageId)
            ?.apply {
                this.status = status.name
            }
            ?.let(MessageFactory::create)
            .orElse { throw MessageNotFound(messageId) }
    }

    override suspend fun updateMessageUserInfo(
        messageId: Identifier,
        info: MessageInfo.FromUserInfo,
    ): Message = transaction {
        val user = UserEntity.findById(info.userId) ?: throw UserNotFound(info.userId)
        val message = MessageEntity.findById(messageId) ?: throw MessageNotFound(messageId)
        val all = MessageForUserEntity.find {
            MessageForUserTable.userId eq info.userId
        }

        val entity = if (all.empty()) {
            MessageForUserEntity.new {
                this.message = message
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

        MessageFactory.create(entity.message)
    }

    override suspend fun removeMessage(
        messageId: Identifier,
    ): Message = transaction {
        MessageEntity.findById(messageId)
            ?.apply { delete() }
            ?.let(MessageFactory::create)
            ?: throw MessageNotFound(messageId)
    }

    override suspend fun getMessage(
        messageId: Identifier,
    ): Message = transaction {
        MessageEntity.findById(messageId)
            ?.let(MessageFactory::create)
            ?: throw MessageNotFound(messageId)
    }

    override suspend fun getMessageInfo(
        messageId: Identifier,
    ): MessageInfo = transaction {
        val messageEntity = MessageEntity.findById(messageId) ?: throw MessageNotFound(messageId)
        val addresseeUsers = messageEntity.addresseeUsers
            .filter { !it.fromGroup }
            .map {
                MessageInfo.FromUserInfo(
                    userId = it.userId.id.value,
                    received = it.received,
                    read = it.read,
                    answer = it.answer,
                )
            }
        val addresseeGroups = messageEntity.addresseeGroups.map { forGroup ->
            val membersIds = forGroup.group.members.map { member -> member.user.id.value }
            val members: List<MessageInfo.FromUserInfo> = MessageForUserEntity.find {
                val byMessageId = (MessageForUserTable.messageId eq messageId)
                val byUserId = (MessageForUserTable.userId inList membersIds)
                val byGroupTrue = (MessageForUserTable.fromGroup eq true)
                byMessageId and byUserId and byGroupTrue
            }.map { forUser ->
                MessageInfo.FromUserInfo(
                    userId = forUser.userId.id.value,
                    received = forUser.received,
                    read = forUser.read,
                    answer = forUser.answer,
                )
            }
            MessageInfo.GroupInfo(
                id = forGroup.group.id.value,
                name = forGroup.group.name,
                members = members,
            )
        }
        MessageInfo(
            message = MessageFactory.create(messageEntity),
            addresseeGroups = addresseeGroups,
            addresseeUsers = addresseeUsers,
            status = MessageInfo.Status.values()
                .find { it.name == messageEntity.status }
                .orElse { MessageInfo.Status.UNDEFINED },
        )
    }

    override suspend fun getMessagesByAuthorId(
        authorId: Identifier,
    ): List<Message> = transaction {
        MessageEntity.find { MessageTable.author eq authorId }
            .map(MessageFactory::create)
    }

    override suspend fun getMessagesByAddresseeUserId(
        userId: Identifier,
    ): List<Message> = transaction {
        MessageForUserEntity
            .find { MessageForUserTable.userId eq userId }
            .map(MessageForUserEntity::message)
            .map(MessageFactory::create)
    }

    override suspend fun getMessagesByAddresseeGroupIds(
        groupIds: Set<Identifier>,
    ): List<Message> = transaction {
        MessageForGroupEntity
            .find { MessageForGroupTable.groupId inList groupIds }
            .map(MessageForGroupEntity::message)
            .map(MessageFactory::create)
    }

    override suspend fun setReceivedMessageAnswer(
        messageId: Identifier,
        userId: Identifier,
        answer: String
    ): Message = transaction {
        MessageForUserEntity.find {
            val byUserId = (MessageForUserTable.userId eq userId)
            val byMessageId = (MessageForUserTable.messageId eq messageId)
            byUserId and byMessageId
        }
            .map { entity -> entity.apply { this.answer = answer } }
            .firstOrNull()
            .orElse { throw MessageNotFound(messageId) }
            .let(MessageForUserEntity::message)
            .let(MessageFactory::create)
    }

    override suspend fun setReceivedMessageNotify(
        messageId: Identifier,
        userId: Identifier,
    ): Message = transaction {
        MessageForUserEntity.find {
            val byUserId = MessageForUserTable.userId eq userId
            val byMessageId = MessageForUserTable.messageId eq messageId
            byUserId and byMessageId
        }
            .map { entity -> entity.apply { this.received = true } }
            .firstOrNull()
            .orElse { throw MessageNotFound(messageId) }
            .let(MessageForUserEntity::message)
            .let(MessageFactory::create)
    }

    override suspend fun setReadMessageNotify(
        messageId: Identifier,
        userId: Identifier,
    ): Message = transaction {
        MessageForUserEntity.find {
            val byUserId = MessageForUserTable.userId eq userId
            val byMessageId = MessageForUserTable.messageId eq messageId
            (byUserId) and (byMessageId)
        }
            .map { entity -> entity.apply { this.read = true } }
            .firstOrNull()
            .orElse { throw MessageNotFound(messageId) }
            .let(MessageForUserEntity::message)
            .let(MessageFactory::create)
    }

}