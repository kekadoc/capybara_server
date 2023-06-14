package com.kekadoc.project.capybara.server.data.source.api.group

import com.kekadoc.project.capybara.server.domain.model.group.Group
import com.kekadoc.project.capybara.server.domain.model.Identifier

interface GroupDataSource {

    suspend fun getAllGroups(): List<Group>

    suspend fun getGroup(groupId: Identifier): Group

    suspend fun findGroup(groupId: Identifier): Group?

    suspend fun getGroups(groupIds: List<Identifier>): List<Group>

    suspend fun findGroups(groupIds: List<Identifier>): List<Group>

    suspend fun createGroup(name: String, members: Set<Identifier>): Group

    suspend fun updateGroupName(groupId: Identifier, name: String): Group

    suspend fun addMembersToGroup(groupId: Identifier, members: Set<Identifier>): Group

    suspend fun removeMembersFromGroup(groupId: Identifier, members: Set<Identifier>): Group

    suspend fun deleteGroup(groupId: Identifier): Group

}