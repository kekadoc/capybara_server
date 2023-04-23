@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kekadoc.project.capybara.server.intercator.groups

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.converter.dto.GroupDtoConverter
import com.kekadoc.project.capybara.server.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.groups.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class GroupsInteractorImpl(
    private val usersRepository: UsersRepository,
    private val groupsRepository: GroupsRepository,
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
        .map(GroupDtoConverter::revert)
        .map(::CreateGroupResponse)
        .single()

    override suspend fun getGroup(
        authToken: String,
        groupId: Identifier,
    ): GetGroupResponse = usersRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { groupsRepository.getGroup(groupId) }
        .map(GroupDtoConverter::revert)
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
        .map(GroupDtoConverter::revert)
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
        .map(GroupDtoConverter::revert)
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
        .map(GroupDtoConverter::revert)
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
        .map(GroupDtoConverter::revert)
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
                    groupId = groupId,
                )
            }
            .single()
    }

}