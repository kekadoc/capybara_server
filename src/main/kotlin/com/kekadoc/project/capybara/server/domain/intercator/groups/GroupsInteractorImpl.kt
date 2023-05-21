package com.kekadoc.project.capybara.server.domain.intercator.groups

import com.kekadoc.project.capybara.server.common.extensions.mapElements
import com.kekadoc.project.capybara.server.data.repository.auth.AuthorizationRepository
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.network.model.SimpleGroupDto
import com.kekadoc.project.capybara.server.data.source.network.model.converter.GroupDtoConverter
import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.domain.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.groups.model.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class GroupsInteractorImpl(
    private val authorizationRepository: AuthorizationRepository,
    private val usersRepository: UsersRepository,
    private val groupsRepository: GroupsRepository,
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
) : GroupsInteractor {

    override suspend fun getAllGroups(): GetAllGroupsResponseDto = groupsRepository.getAllGroups()
        .mapElements { group ->
            SimpleGroupDto(
                id = group.id,
                name = group.name,
            )
        }
        .map(::GetAllGroupsResponseDto)
        .single()

    override suspend fun createGroup(
        authToken: Token,
        request: CreateGroupRequest,
    ): CreateGroupResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.createGroup(
                request.name,
                request.members.toSet(),
            )
        }
        .map(GroupDtoConverter::convert)
        .map(::CreateGroupResponse)
        .single()

    override suspend fun getGroup(
        authToken: Token,
        groupId: Identifier,
    ): GetGroupResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { groupsRepository.getGroup(groupId) }
        .map(GroupDtoConverter::convert)
        .map(::GetGroupResponseDto)
        .single()

    override suspend fun updateGroupName(
        authToken: Token,
        groupId: Identifier,
        request: UpdateGroupNameRequest,
    ): UpdateGroupResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.updateGroupName(
                groupId = groupId,
                name = request.name,
            )
        }
        .map(GroupDtoConverter::convert)
        .map(::UpdateGroupResponse)
        .single()

    override suspend fun addMembersToGroup(
        authToken: Token,
        groupId: Identifier,
        request: UpdateGroupMembersRequest,
    ): UpdateGroupResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.addMembersToGroup(
                groupId = groupId,
                members = request.members,
            )
        }
        .map(GroupDtoConverter::convert)
        .map(::UpdateGroupResponse)
        .single()

    override suspend fun removeMembersFromGroup(
        authToken: Token,
        groupId: Identifier,
        request: UpdateGroupMembersRequest,
    ): UpdateGroupResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.removeMembersFromGroup(
                groupId = groupId,
                members = request.members,
            )
        }
        .map(GroupDtoConverter::convert)
        .map(::UpdateGroupResponse)
        .single()

    override suspend fun deleteGroup(
        authToken: Token,
        groupId: Identifier
    ) = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.deleteGroup(
                groupId = groupId,
            )
        }
        .collect()

}