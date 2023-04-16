package com.kekadoc.project.capybara.server.data.source.group

import com.google.firebase.database.FirebaseDatabase
import com.kekadoc.project.capybara.server.common.exception.EntityNotFoundException
import com.kekadoc.project.capybara.server.common.extensions.*
import com.kekadoc.project.capybara.server.data.model.Group
import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class GroupDataSourceImpl(
    database: FirebaseDatabase,
) : GroupDataSource {

    private val groups = database.getReference("/groups")


    override fun getGroup(groupId: Identifier): Flow<Group> = flowOf {
        groups.child(groupId).get<Group>() ?: throw EntityNotFoundException("Group by $groupId not found")
    }

    override fun createGroup(
        name: String,
        members: Set<Identifier>,
    ): Flow<Group> = flowOf {
        val document = groups.push()
        val group = Group(
            id = document.key,
            name = name,
            members = members.toList(),
        )
        document.set(group)
        group
    }

    override fun updateGroup(
        groupId: Identifier,
        name: String,
        members: Set<Identifier>
    ): Flow<Group> = flowOf {
        groups.child(groupId).runTransaction { currentGroup ->
            currentGroup?.copy(
                name = name,
                members = members.toList(),
            )
        } ?: throw EntityNotFoundException("Not found group $groupId for update")
    }

    override fun updateGroupName(groupId: Identifier, name: String): Flow<Group> = flowOf {
        groups.child(groupId).runTransaction { currentGroup ->
            currentGroup?.copy(
                name = name,
            )
        } ?: throw EntityNotFoundException("Not found group $groupId for update")
    }

    override fun addMembersToGroup(
        groupId: Identifier,
        members: Set<Identifier>,
    ): Flow<Group> = flowOf {
        groups.child(groupId).runTransaction { currentGroup ->
            currentGroup?.copy(
                members = (currentGroup.members + members).distinct(),
            )
        } ?: throw EntityNotFoundException("Not found group $groupId for update")
    }

    override fun removeMembersFromGroup(
        groupId: Identifier,
        members: Set<Identifier>,
    ): Flow<Group> = flowOf {
        groups.child(groupId).runTransaction { currentGroup ->
            currentGroup?.copy(
                members = (currentGroup.members - members).distinct(),
            )
        } ?: throw EntityNotFoundException("Not found group $groupId for update")
    }

    override fun deleteGroup(groupId: Identifier): Flow<Group> = getGroup(groupId)
        .onEach { groups.child(groupId).remove() }

}