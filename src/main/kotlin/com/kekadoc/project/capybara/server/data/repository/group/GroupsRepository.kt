package com.kekadoc.project.capybara.server.data.repository.group

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.group.Group
import kotlinx.coroutines.flow.Flow

interface GroupsRepository {

    fun getStudentGroups(): Flow<List<Group>>

    fun getAllGroups(): Flow<List<Group>>

    fun getGroup(groupId: Identifier): Flow<Group>

    fun findGroup(groupId: Identifier): Flow<Group?>

    fun getGroups(groupIds: List<Identifier>): Flow<List<Group>>

    fun findGroups(groupIds: List<Identifier>): Flow<List<Group>>

    fun createGroup(name: String, type: Group.Type, members: Set<Identifier>): Flow<Group>

    fun updateGroupName(groupId: Identifier, name: String): Flow<Group>

    fun addMembersToGroup(groupId: Identifier, members: Set<Identifier>): Flow<Group>

    fun removeMembersFromGroup(groupId: Identifier, members: Set<Identifier>): Flow<Group>

    fun deleteGroup(groupId: Identifier): Flow<Group>

}