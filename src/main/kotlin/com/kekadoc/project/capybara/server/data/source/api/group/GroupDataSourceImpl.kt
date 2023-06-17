package com.kekadoc.project.capybara.server.data.source.api.group

import com.kekadoc.project.capybara.server.common.exception.GroupNotFound
import com.kekadoc.project.capybara.server.common.exception.UserNotFound
import com.kekadoc.project.capybara.server.common.extensions.orElse
import com.kekadoc.project.capybara.server.data.source.database.entity.GroupEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserGroupEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.converter.GroupEntityConverter
import com.kekadoc.project.capybara.server.data.source.database.table.GroupsTable
import com.kekadoc.project.capybara.server.data.source.database.table.UsersGroupsTable
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.group.Group
import org.jetbrains.exposed.sql.transactions.transaction

class GroupDataSourceImpl : GroupDataSource {

    override suspend fun getStudentGroups(): List<Group> = transaction {
        GroupEntity.find { GroupsTable.type eq Group.Type.STUDENT.name }
            .map(GroupEntityConverter::convert)
    }

    override suspend fun getAllGroups(): List<Group> = transaction {
        GroupEntity.all().map(GroupEntityConverter::convert)
    }

    override suspend fun getGroup(
        groupId: Identifier,
    ): Group = findGroup(groupId) ?: throw GroupNotFound(id = groupId)

    override suspend fun findGroup(groupId: Identifier): Group? = transaction {
        GroupEntity.findById(groupId)
            ?.let(GroupEntityConverter::convert)
    }

    override suspend fun getGroups(
        groupIds: List<Identifier>,
    ): List<Group> = findGroups(groupIds).also { groups ->
        groupIds.forEach { targetGroupId ->
            if (groups.find { group -> group.id == targetGroupId } == null) {
                throw GroupNotFound(id = targetGroupId)
            }
        }
    }

    override suspend fun findGroups(
        groupIds: List<Identifier>
    ): List<Group> = transaction {
        GroupEntity.find { GroupsTable.id inList groupIds }
            .map(GroupEntityConverter::convert)
    }

    override suspend fun createGroup(
        name: String,
        type: Group.Type,
        members: Set<Identifier>,
    ): Group = transaction {
        val groupEntity = GroupEntity.new {
            this.name = name
            this.type = type.name
        }
        members.forEach { userId ->
            UserGroupEntity.new {
                this.group = groupEntity
                this.user = UserEntity.findById(userId) ?: throw UserNotFound(userId)
            }
        }
        GroupEntityConverter.convert(groupEntity)
    }

    override suspend fun updateGroupName(
        groupId: Identifier,
        name: String,
    ): Group = transaction {
        GroupEntity.findById(groupId)
            ?.apply { this.name = name }
            ?.let(GroupEntityConverter::convert)
            .orElse { throw GroupNotFound(id = groupId) }
    }

    override suspend fun addMembersToGroup(
        groupId: Identifier,
        members: Set<Identifier>,
    ): Group = transaction {
        val groupEntity = GroupEntity.findById(groupId) ?: throw GroupNotFound(groupId)
        val currentMembers = groupEntity.members
        members.filter { newMember -> currentMembers.all { it.user.id.value != newMember } }
            .forEach { newMemberId ->
                UserGroupEntity.new {
                    this.group = groupEntity
                    this.user = UserEntity.findById(newMemberId) ?: throw UserNotFound(newMemberId)
                }
            }
        groupEntity.let(GroupEntityConverter::convert)
    }

    override suspend fun removeMembersFromGroup(
        groupId: Identifier,
        members: Set<Identifier>,
    ): Group = transaction {
        val groupEntity = GroupEntity.findById(groupId) ?: throw GroupNotFound(groupId)
        UserGroupEntity.find { UsersGroupsTable.user inList members }
            .forEach(UserGroupEntity::delete)
        groupEntity.let(GroupEntityConverter::convert)
    }

    override suspend fun deleteGroup(
        groupId: Identifier,
    ): Group = transaction {
        GroupEntity.findById(groupId)
            ?.apply { delete() }
            ?.let(GroupEntityConverter::convert)
            .orElse { throw GroupNotFound(id = groupId) }
    }

}