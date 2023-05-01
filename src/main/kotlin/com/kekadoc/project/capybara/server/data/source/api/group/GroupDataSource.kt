package com.kekadoc.project.capybara.server.data.source.api.group

import com.kekadoc.project.capybara.server.data.model.Group
import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.coroutines.flow.Flow

interface GroupDataSource {

    suspend fun getGroup(groupId: Identifier): Group?

    suspend fun getGroups(groupIds: List<Identifier>): List<Group>

    suspend fun createGroup(name: String, members: Set<Identifier>): Group

    suspend fun updateGroupName(groupId: Identifier, name: String): Group?

    suspend fun addMembersToGroup(groupId: Identifier, members: Set<Identifier>): Group?

    suspend fun removeMembersFromGroup(groupId: Identifier, members: Set<Identifier>): Group?

    suspend fun deleteGroup(groupId: Identifier): Group?

}