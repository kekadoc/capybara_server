package com.kekadoc.project.capybara.server.data.source.api.messages

import com.kekadoc.project.capybara.server.Server
import com.kekadoc.project.capybara.server.common.exception.EntityNotFoundException
import com.kekadoc.project.capybara.server.common.exception.GroupNotFound
import com.kekadoc.project.capybara.server.common.exception.MessageNotFound
import com.kekadoc.project.capybara.server.common.exception.UserNotFound
import com.kekadoc.project.capybara.server.common.extensions.orElse
import com.kekadoc.project.capybara.server.data.source.database.entity.*
import com.kekadoc.project.capybara.server.data.source.database.entity.factory.MessageFactory
import com.kekadoc.project.capybara.server.data.source.database.entity.factory.MessageForUserFactory
import com.kekadoc.project.capybara.server.data.source.database.table.MessageForUserTable
import com.kekadoc.project.capybara.server.data.source.database.table.MessageTable
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.common.Range
import com.kekadoc.project.capybara.server.domain.model.message.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MessagesDataSourceImpl : MessagesDataSource {

    override suspend fun createMessage(
        authorId: Identifier,
        type: MessageType,
        title: String?,
        text: String,
        actions: List<MessageAction>?,
        isMultiAction: Boolean,
        addresseeUsers: List<Identifier>,
        addresseeGroups: Map<Identifier, List<Identifier>?>,
        notifications: MessageNotifications
    ): Message = transaction {
        val messageEntity = MessageEntity.new {
            this.author = UserEntity.findById(authorId) ?: throw UserNotFound(authorId)
            this.type = type.name
            this.contentTitle = title
            this.contentText = text
            this.actions = actions?.map(MessageAction::text)?.toTypedArray()
            this.date = Server.getLocalDateTime()
            this.status = MessageStatus.RECEIVED.toString()
            this.notificationEmail = notifications.email
            this.notificationApp = notifications.app
            this.notificationMessengers = notifications.messengers
        }

        val addresseeGroupsEntity: List<MessageForGroupEntity> = addresseeGroups.map { (groupId, membersIds) ->
            val group = GroupEntity.findById(groupId) ?: throw GroupNotFound(groupId)
            group.members
                .filter { membersIds == null || membersIds.contains(it.id.value) }
                .forEach { member ->
                    MessageForUserEntity.new {
                        this.message = messageEntity
                        this.userId = member.user
                        this.asGroupMember = group
                    }
                }
            MessageForGroupEntity.new {
                this.message = messageEntity
                this.group = group
            }
        }

        val addresseeUsersEntity = addresseeUsers.map { userId ->
            MessageForUserEntity.new {
                this.message = messageEntity
                this.userId = UserEntity.findById(userId) ?: throw UserNotFound(userId)
            }
        }

        MessageFactory.create(
            value = messageEntity,
            messageForUserEntity = addresseeUsersEntity,
            messageForGroupEntity = addresseeGroupsEntity,
            actions = actions,
        )
    }

    override suspend fun updateMessageStatus(
        messageId: Identifier,
        status: MessageStatus,
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
                this.answerIds = info.answerIds?.map(Long::toString)?.toTypedArray()
            }
        } else {
            all.first().apply {
                this.received = info.received
                this.read = info.read
                this.answerIds = info.answerIds?.map(Long::toString)?.toTypedArray()
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

    override suspend fun getAddresseeUserInfo(
        messageId: Identifier,
        userId: Identifier,
    ): MessageForUser = transaction {
        val entity = MessageForUserEntity.find {
            val isUserEq = MessageForUserTable.userId eq userId
            val isMessageEq = MessageForUserTable.messageId eq messageId
            isUserEq and isMessageEq
        }.singleOrNull() ?: throw EntityNotFoundException("MessageForUserEntity not found")
        MessageForUserFactory.create(entity)
    }

    override suspend fun getMessageInfo(
        messageId: Identifier,
    ): MessageInfo = transaction {
        val messageEntity = MessageEntity.findById(messageId) ?: throw MessageNotFound(messageId)
        val addresseeUsers = messageEntity.addresseeUsers
            .filter { it.asGroupMember == null }
            .map { forUser ->
                MessageInfo.FromUserInfo(
                    userId = forUser.userId.id.value,
                    received = forUser.received,
                    read = forUser.read,
                    answerIds = forUser.answerIds?.map(String::toLong),
                )
            }
        val addresseeGroups = messageEntity.addresseeGroups.map { forGroup ->
            val membersIds = forGroup.group.members.map { member -> member.user.id.value }
            val members: List<MessageInfo.FromUserInfo> = MessageForUserEntity.find {
                val byMessageId = (MessageForUserTable.messageId eq messageId)
                val byUserId = (MessageForUserTable.userId inList membersIds)
                val byGroupTrue = (MessageForUserTable.asGroupMember neq null)
                byMessageId and byUserId and byGroupTrue
            }.map { forUser ->
                MessageInfo.FromUserInfo(
                    userId = forUser.userId.id.value,
                    received = forUser.received,
                    read = forUser.read,
                    answerIds = forUser.answerIds?.map(String::toLong),
                )
            }
            MessageInfo.GroupInfo(
                groupId = forGroup.group.id.value,
                name = forGroup.group.name,
                members = members,
            )
        }
        MessageInfo(
            message = MessageFactory.create(messageEntity),
            addresseeGroups = addresseeGroups,
            addresseeUsers = addresseeUsers,
            status = MessageStatus.values()
                .find { it.name == messageEntity.status }
                .orElse { MessageStatus.UNDEFINED },
        )
    }

    override suspend fun getMessagesByAuthorId(
        authorId: Identifier,
        range: Range
    ): List<Message> = transaction {
        MessageEntity.find { MessageTable.author eq authorId }
            .orderBy(MessageTable.date to SortOrder.DESC)
            .limit(n = range.count, offset = range.from.toLong())
            .map(MessageFactory::create)
    }

    override suspend fun getMessagesByAddresseeUserId(
        userId: Identifier,
        range: Range,
    ): List<Message> = transaction {
        val messagesForUser = MessageForUserTable.join(
            otherTable = MessageTable,
            joinType = JoinType.INNER,
            onColumn = MessageForUserTable.messageId,
            otherColumn = MessageTable.id,
        )
            .selectAll()
            .andWhere { MessageForUserTable.userId eq userId }
            .orderBy(MessageTable.date, SortOrder.DESC)
            .limit(n = range.count, offset = range.from.toLong())
            .toList()
            .map { row -> row[MessageTable.id].value }

        MessageEntity.forIds(messagesForUser)
            .orderBy(MessageTable.date to SortOrder.DESC)
            .map(MessageFactory::create)

//        MessageForUserEntity.find { MessageForUserTable.userId eq userId }
//            .limit(n = range.count, offset = range.from.toLong())
//            .map(MessageForUserEntity::message)
//            .map(MessageFactory::create)
    }

    override suspend fun setReceivedMessageAnswer(
        messageId: Identifier,
        userId: Identifier,
        answerIds: List<Long>
    ): Message  = transaction {
        MessageForUserEntity.find {
            val byUserId = (MessageForUserTable.userId eq userId)
            val byMessageId = (MessageForUserTable.messageId eq messageId)
            byUserId and byMessageId
        }
            .map { entity ->
                entity.apply {
                    this.answerIds = answerIds.map(Long::toString).toTypedArray()
                    this.received = true
                    this.read = true
                }
            }
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