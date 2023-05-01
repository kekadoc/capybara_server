package com.kekadoc.project.capybara.server.data.source.api.user.access

import com.kekadoc.project.capybara.server.common.exception.EntityNotFoundException
import com.kekadoc.project.capybara.server.common.exception.GroupNotFound
import com.kekadoc.project.capybara.server.common.exception.UserNotFound
import com.kekadoc.project.capybara.server.common.extensions.orElse
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.access.UserAccessToGroup
import com.kekadoc.project.capybara.server.data.model.access.UserAccessToUser
import com.kekadoc.project.capybara.server.data.source.DataSourceException
import com.kekadoc.project.capybara.server.data.source.converter.entity.UserAccessToGroupEntityConverter
import com.kekadoc.project.capybara.server.data.source.converter.entity.UserAccessToUserEntityConverter
import com.kekadoc.project.capybara.server.data.source.database.entity.*
import com.kekadoc.project.capybara.server.data.source.database.table.UserAccessToGroupTable
import com.kekadoc.project.capybara.server.data.source.database.table.UserAccessToUserTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class UserAccessDataSourceImpl : UserAccessDataSource {

    override suspend fun getAllAccessForUser(
        userId: Identifier,
    ): List<UserAccessToUser> = transaction {
        UserAccessToUserEntity.find { (UserAccessToUserTable.fromUser eq userId) }
            .map(UserAccessToUserEntityConverter::convert)
    }

    override suspend fun getAccessForUser(
        userId: Identifier,
        forUserId: Identifier,
    ): UserAccessToUser? = transaction {
        UserAccessToUserEntity.find {
            (UserAccessToUserTable.fromUser eq userId) and (UserAccessToUserTable.toUser eq forUserId)
        }
            .firstOrNull()
            ?.let(UserAccessToUserEntityConverter::convert)
            .orElse { UserAccessToUser.nothing(fromUserId = userId, toUserId = forUserId) }
    }

    override fun getAccessForUsers(
        userId: Identifier,
        forUserIds: List<Identifier>,
    ): List<UserAccessToUser> = transaction {
        val list = UserAccessToUserEntity.find {
            (UserAccessToUserTable.fromUser eq userId) and (UserAccessToUserTable.toUser inList forUserIds)
        }
            .map(UserAccessToUserEntityConverter::convert)
        forUserIds.map { forUserId ->
            list.find { it.toUserId == forUserId } ?: UserAccessToUser.nothing(userId, forUserId)
        }
    }

    override suspend fun updateAccessForUser(
        userId: Identifier,
        forUserId: Identifier,
        userAccessUser: UserAccessToUser.Updater,
    ): UserAccessToUser? = transaction {
        UserAccessToUserEntity.find {
            (UserAccessToUserTable.fromUser eq userId) and (UserAccessToUserTable.toUser eq forUserId)
        }
            .firstOrNull()
            ?.apply {
                userAccessUser.readProfile
                    ?.also { readProfile -> this.readProfile = readProfile }
                userAccessUser.contactInfo
                    ?.also { contactInfo -> this.contactInfo = contactInfo }
                userAccessUser.sentNotification
                    ?.also { sentNotification -> this.sentNotification = sentNotification }
            }
            .orElse {
                val fromUser = UserEntity.findById(userId) ?: return@orElse null
                val toUser = UserEntity.findById(forUserId) ?: return@orElse null
                UserAccessToUserEntity.new {
                    this.fromUser = fromUser
                    this.toUser = toUser
                    userAccessUser.readProfile?.also { readProfile -> this.readProfile = readProfile }
                    userAccessUser.contactInfo?.also { contactInfo -> this.contactInfo = contactInfo }
                    userAccessUser.sentNotification?.also { sentNotification ->
                        this.sentNotification = sentNotification
                    }
                }
            }
            ?.let(UserAccessToUserEntityConverter::convert)
    }


    override suspend fun getAllAccessForGroup(
        userId: Identifier,
    ): List<UserAccessToGroup> = transaction {
        UserAccessToGroupEntity.find { (UserAccessToGroupTable.user eq userId) }
            .map(UserAccessToGroupEntityConverter::convert)
    }

    override suspend fun getAccessForGroup(
        userId: Identifier,
        groupId: Identifier,
    ): UserAccessToGroup? = transaction {
        UserAccessToGroupEntity.find {
            (UserAccessToGroupTable.user eq userId) and (UserAccessToGroupTable.group eq groupId)
        }
            .firstOrNull()
            ?.let(UserAccessToGroupEntityConverter::convert)
            .orElse { UserAccessToGroup.nothing(userId = userId, groupId = groupId) }
    }

    override suspend fun getAccessForGroup(
        userId: Identifier,
        groupIds: List<Identifier>,
    ): List<UserAccessToGroup> = transaction {
        val list = UserAccessToGroupEntity.find {
            (UserAccessToGroupTable.user eq userId) and (UserAccessToGroupTable.group inList groupIds)
        }
            .map(UserAccessToGroupEntityConverter::convert)
        groupIds.map { forGroupId ->
            list.find { it.groupId == forGroupId } ?: UserAccessToGroup.nothing(userId, forGroupId)
        }
    }

    override suspend fun updateAccessForGroup(
        userId: Identifier,
        groupId: Identifier,
        userAccessGroup: UserAccessToGroup.Updater,
    ): UserAccessToGroup? = transaction {
        UserAccessToGroupEntity.find {
            (UserAccessToGroupTable.user eq userId) and (UserAccessToGroupTable.group eq groupId)
        }
            .firstOrNull()
            ?.apply {
                userAccessGroup.readInfo
                    ?.also { readInfo -> this.readInfo = readInfo }
                userAccessGroup.readMembers
                    ?.also { readMembers -> this.readMembers = readMembers }
                userAccessGroup.sentNotification
                    ?.also { sentNotification -> this.sentNotification = sentNotification }
            }
            .orElse {
                val user = UserEntity.findById(userId) ?: throw UserNotFound(userId)
                val group = GroupEntity.findById(groupId) ?: throw GroupNotFound(groupId)
                UserAccessToGroupEntity.new {
                    this.user = user
                    this.group = group
                    userAccessGroup.readInfo?.also { readInfo -> this.readInfo = readInfo }
                    userAccessGroup.readMembers?.also { readMembers -> this.readMembers = readMembers }
                    userAccessGroup.sentNotification?.also { sentNotification -> this.sentNotification = sentNotification }
                }
            }
            ?.let(UserAccessToGroupEntityConverter::convert)
    }

}