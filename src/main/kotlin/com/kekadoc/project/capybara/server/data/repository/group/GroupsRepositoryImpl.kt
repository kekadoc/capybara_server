package com.kekadoc.project.capybara.server.data.repository.group

import com.kekadoc.project.capybara.server.data.source.api.group.GroupDataSource
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.group.Group
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GroupsRepositoryImpl(
    private val dataSource: GroupDataSource,
) : GroupsRepository {

    override fun getStudentGroups(): Flow<List<Group>> = flowOf {
        dataSource.getStudentGroups()
    }

    override fun getAllGroups(): Flow<List<Group>> = flowOf {
        dataSource.getAllGroups()
    }

    override fun getGroups(groupIds: List<Identifier>): Flow<List<Group>> = flowOf {
        dataSource.getGroups(groupIds)
    }

    override fun findGroups(groupIds: List<Identifier>): Flow<List<Group>> = flowOf {
        dataSource.findGroups(groupIds)
    }

    override fun getGroup(groupId: Identifier): Flow<Group> = flowOf {
        dataSource.getGroup(groupId)
    }

    override fun findGroup(groupId: Identifier): Flow<Group?> = flowOf {
        dataSource.findGroup(groupId)
    }

    override fun createGroup(name: String, type: Group.Type, members: Set<Identifier>): Flow<Group> = flowOf {
        dataSource.createGroup(name, type, members)
    }

    override fun updateGroupName(groupId: Identifier, name: String): Flow<Group> = flowOf {
        dataSource.updateGroupName(groupId, name)
    }

    override fun addMembersToGroup(groupId: Identifier, members: Set<Identifier>): Flow<Group> = flowOf {
        dataSource.addMembersToGroup(groupId, members)
    }

    override fun removeMembersFromGroup(
        groupId: Identifier,
        members: Set<Identifier>,
    ): Flow<Group> = flowOf {
        dataSource.removeMembersFromGroup(groupId, members)
    }

    override fun deleteGroup(groupId: Identifier): Flow<Group> = flowOf {
        dataSource.deleteGroup(groupId)
    }

}