package com.kekadoc.project.capybara.server.domain.intercator.groups

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.repository.auth.AuthorizationRepository
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.domain.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.user.Profile
import com.kekadoc.project.capybara.server.domain.model.user.User
import com.kekadoc.project.capybara.server.domain.model.user.isAdmin
import com.kekadoc.project.capybara.server.routing.api.groups.model.*
import com.kekadoc.project.capybara.server.routing.model.converter.GroupDtoConverter
import com.kekadoc.project.capybara.server.routing.model.factory.ProfileDtoFactory
import com.kekadoc.project.capybara.server.routing.model.group.GroupDto
import com.kekadoc.project.capybara.server.routing.model.group.SimpleGroupDto
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

    override suspend fun getAllGroupsWithMembers(): GetAllGroupsWithMembersResponseDto =
        groupsRepository.getAllGroups()
            .mapElements { group ->
                GroupDto(
                    id = group.id,
                    name = group.name,
                    membersIds = group.members.map(User::id),
                )
            }
            .map(::GetAllGroupsWithMembersResponseDto)
            .single()

    override suspend fun createGroup(
        authToken: Token,
        request: CreateGroupRequestDto,
    ): CreateGroupResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.createGroup(
                request.name,
                request.members.toSet(),
            )
        }
        .map(GroupDtoConverter::convert)
        .map(::CreateGroupResponseDto)
        .single()

    override suspend fun getGroup(
        authToken: Token,
        groupId: Identifier,
    ): GetGroupResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapConcat { user ->
            usersRepository.getAccessForGroup(user.id, groupId)
                .map { it.readInfo && it.readMembers || user.type == Profile.Type.ADMIN }
        }
        .onEach { isAvailable ->
            if (!isAvailable) throw HttpException(HttpStatusCode.Forbidden)
            else Unit
        }
        .flatMapLatest { groupsRepository.getGroup(groupId) }
        .map(GroupDtoConverter::convert)
        .map(::GetGroupResponseDto)
        .single()

    override suspend fun getGroups(
        authToken: String,
        groupIds: List<Identifier>,
    ): GetGroupListResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapConcat { user ->
            usersRepository.getAccessForGroup(user.id, groupIds)
                .map { listOfAccess ->
                    user.type == Profile.Type.ADMIN || listOfAccess.all { it.readInfo }
                }
        }
        .onEach { isAvailable ->
            if (!isAvailable) throw HttpException(HttpStatusCode.Forbidden)
            else Unit
        }
        .flatMapLatest { groupsRepository.getGroups(groupIds) }
        .mapElements { SimpleGroupDto(it.id, it.name) }
        .map(::GetGroupListResponseDto)
        .single()

    override suspend fun getGroupsWithMembers(
        authToken: String,
        groupIds: List<Identifier>,
    ): GetGroupWithMembersListResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapConcat { user ->
            usersRepository.getAccessForGroup(user.id, groupIds)
                .map { listOfAccess ->
                    user.type == Profile.Type.ADMIN || listOfAccess.all { it.readInfo }
                }
        }
        .onEach { isAvailable ->
            if (!isAvailable) throw HttpException(HttpStatusCode.Forbidden)
            else Unit
        }
        .flatMapLatest { groupsRepository.getGroups(groupIds) }
        .mapElements { GroupDto(it.id, it.name, it.members.map(User::id)) }
        .map(::GetGroupWithMembersListResponseDto)
        .single()

    override suspend fun updateGroupName(
        authToken: Token,
        groupId: Identifier,
        request: UpdateGroupNameRequestDto,
    ): UpdateGroupResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.updateGroupName(
                groupId = groupId,
                name = request.name,
            )
        }
        .map(GroupDtoConverter::convert)
        .map(::UpdateGroupResponseDto)
        .single()

    override suspend fun addMembersToGroup(
        authToken: Token,
        groupId: Identifier,
        request: UpdateGroupMembersRequestDto,
    ): UpdateGroupResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.addMembersToGroup(
                groupId = groupId,
                members = request.members,
            )
        }
        .map(GroupDtoConverter::convert)
        .map(::UpdateGroupResponseDto)
        .single()

    override suspend fun removeMembersFromGroup(
        authToken: Token,
        groupId: Identifier,
        request: UpdateGroupMembersRequestDto,
    ): UpdateGroupResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            groupsRepository.removeMembersFromGroup(
                groupId = groupId,
                members = request.members,
            )
        }
        .map(GroupDtoConverter::convert)
        .map(::UpdateGroupResponseDto)
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

    override suspend fun getGroupMembers(
        authToken: String,
        groupId: Identifier
    ): GetGroupMembersResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            usersRepository.getAccessForGroup(userId = user.id, groupId = groupId)
                .flatMapConcat { access ->
                    if (!access.readMembers && !user.isAdmin()) throw HttpException(HttpStatusCode.Forbidden)
                    groupsRepository.getGroup(groupId)
                }
                .map { group -> group.members }
                .mapElements(ProfileDtoFactory::create)
        }
        .map(::GetGroupMembersResponseDto)
        .single()

}