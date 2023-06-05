package com.kekadoc.project.capybara.server.domain.intercator.groups

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.routing.api.groups.model.*

interface GroupsInteractor {

    suspend fun getAllGroups(): GetAllGroupsResponseDto

    suspend fun getAllGroupsWithMembers(): GetAllGroupsWithMembersResponseDto

    suspend fun createGroup(
        authToken: String,
        request: CreateGroupRequest,
    ): CreateGroupResponse

    suspend fun getGroup(
        authToken: String,
        groupId: Identifier,
    ): GetGroupResponseDto

    suspend fun getGroups(
        authToken: String,
        groupIds: List<Identifier>,
    ): GetGroupListResponseDto

    suspend fun getGroupsWithMembers(
        authToken: String,
        groupIds: List<Identifier>,
    ): GetGroupWithMembersListResponseDto

    suspend fun updateGroupName(
        authToken: String,
        groupId: Identifier,
        request: UpdateGroupNameRequest,
    ): UpdateGroupResponse

    suspend fun addMembersToGroup(
        authToken: String,
        groupId: Identifier,
        request: UpdateGroupMembersRequest,
    ): UpdateGroupResponse

    suspend fun removeMembersFromGroup(
        authToken: String,
        groupId: Identifier,
        request: UpdateGroupMembersRequest,
    ): UpdateGroupResponse

    suspend fun deleteGroup(
        authToken: String,
        groupId: Identifier,
    )

    suspend fun getGroupMembers(
        authToken: String,
        groupId: Identifier,
    ): GetGroupMembersResponseDto

}