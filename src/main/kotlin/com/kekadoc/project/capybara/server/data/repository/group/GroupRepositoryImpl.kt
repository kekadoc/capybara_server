package com.kekadoc.project.capybara.server.data.repository.group

import com.kekadoc.project.capybara.server.data.model.Group
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.source.api.group.GroupDataSource
import kotlinx.coroutines.flow.Flow

class GroupRepositoryImpl(
    private val dataSource: GroupDataSource,
) : GroupRepository {

    override fun getGroup(groupId: Identifier): Flow<Group> {
        return dataSource.getGroup(groupId)
    }

    override fun createGroup(name: String, members: Set<Identifier>): Flow<Group> {
        return dataSource.createGroup(name, members)
    }

    override fun updateGroup(
        groupId: Identifier,
        name: String,
        members: Set<Identifier>,
    ): Flow<Group> {
        return dataSource.updateGroup(groupId, name, members)
    }

    override fun updateGroupName(groupId: Identifier, name: String): Flow<Group> {
        return dataSource.updateGroupName(groupId, name)
    }

    override fun addMembersToGroup(groupId: Identifier, members: Set<Identifier>): Flow<Group> {
        return dataSource.addMembersToGroup(groupId, members)
    }

    override fun removeMembersFromGroup(
        groupId: Identifier,
        members: Set<Identifier>,
    ): Flow<Group> {
        return dataSource.removeMembersFromGroup(groupId, members)
    }

    override fun deleteGroup(groupId: Identifier): Flow<Group> {
        return dataSource.deleteGroup(groupId)
    }

}