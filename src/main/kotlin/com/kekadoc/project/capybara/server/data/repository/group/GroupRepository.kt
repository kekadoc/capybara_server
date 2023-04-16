package com.kekadoc.project.capybara.server.data.repository.group

import com.kekadoc.project.capybara.server.data.model.Group
import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.coroutines.flow.Flow

interface GroupRepository {

    fun getGroup(groupId: Identifier): Flow<Group>

    fun createGroup(name: String, members: Set<Identifier>): Flow<Group>

    fun updateGroup(groupId: Identifier, name: String, members: Set<Identifier>): Flow<Group>

    fun updateGroupName(groupId: Identifier, name: String): Flow<Group>

    fun addMembersToGroup(groupId: Identifier, members: Set<Identifier>): Flow<Group>

    fun removeMembersFromGroup(groupId: Identifier, members: Set<Identifier>): Flow<Group>

    fun deleteGroup(groupId: Identifier): Flow<Group>

}