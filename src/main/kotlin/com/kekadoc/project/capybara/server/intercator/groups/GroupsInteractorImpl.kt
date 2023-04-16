@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kekadoc.project.capybara.server.intercator.groups

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.repository.group.GroupRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.groups.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class GroupsInteractorImpl(
    private val usersRepository: UsersRepository,
    private val groupsRepository: GroupRepository,
) : GroupsInteractor {

    override suspend fun createGroup(
        authToken: String,
        request: CreateGroupRequest,
    ): CreateGroupResponse = usersRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.createGroup(
                request.name,
                request.members.toSet(),
            )
        }
        .onEach { newGroup ->
            newGroup.members.map { memberId ->
                usersRepository.updateUserGroupAdd(
                    id = memberId,
                    groups = setOf(newGroup.id),
                )
            }.merge().collect()
        }
        .map(::CreateGroupResponse)
        .single()

    override suspend fun getGroup(
        authToken: String,
        groupId: Identifier,
    ): GetGroupResponse = usersRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.getGroup(groupId)
        }
        .map(::GetGroupResponse)
        .single()

    override suspend fun updateGroup(
        authToken: String,
        groupId: Identifier,
        request: UpdateGroupRequest,
    ): UpdateGroupResponse = usersRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.updateGroup(
                groupId = groupId,
                name = request.name,
                members = request.members,
            )
        }
        .onEach { newGroup ->
            newGroup.members.map { memberId ->
                usersRepository.updateUserGroupAdd(
                    id = memberId,
                    groups = setOf(newGroup.id),
                )
            }.merge().collect()
        }
        .map(::UpdateGroupResponse)
        .single()

    override suspend fun updateGroupName(
        authToken: String,
        groupId: Identifier,
        request: UpdateGroupNameRequest,
    ): UpdateGroupResponse = usersRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.updateGroupName(
                groupId = groupId,
                name = request.name,
            )
        }
        .map(::UpdateGroupResponse)
        .single()

    override suspend fun addMembersToGroup(
        authToken: String,
        groupId: Identifier,
        request: UpdateGroupMembersRequest,
    ): UpdateGroupResponse = usersRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.addMembersToGroup(
                groupId = groupId,
                members = request.members,
            )
        }
        .onEach { newGroup ->
            request.members.map { memberId ->
                usersRepository.updateUserGroupAdd(
                    id = memberId,
                    groups = setOf(newGroup.id),
                )
            }.merge().collect()
        }
        .map(::UpdateGroupResponse)
        .single()

    override suspend fun removeMembersFromGroup(
        authToken: String,
        groupId: Identifier,
        request: UpdateGroupMembersRequest,
    ): UpdateGroupResponse = usersRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.removeMembersFromGroup(
                groupId = groupId,
                members = request.members,
            )
        }
        .onEach { newGroup ->
            request.members.map { memberId ->
                usersRepository.updateUserGroupRemove(
                    id = memberId,
                    groups = setOf(newGroup.id),
                )
            }.merge().collect()
        }
        .map(::UpdateGroupResponse)
        .single()

    override suspend fun deleteGroup(
        authToken: String,
        groupId: Identifier
    ) {
        usersRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                groupsRepository.deleteGroup(
                    groupId = groupId
                )
            }
            .onEach { group ->
                group.members.map { memberId ->
                    usersRepository.updateUserGroupRemove(
                        id = memberId,
                        groups = setOf(group.id),
                    )
                }.merge().collect()
            }
            .single()
    }
}