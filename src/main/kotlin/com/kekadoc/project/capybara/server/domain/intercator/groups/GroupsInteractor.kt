package com.kekadoc.project.capybara.server.domain.intercator.groups

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.routing.api.groups.model.*

interface GroupsInteractor {

    suspend fun getAllGroups(): GetAllGroupsResponseDto

    suspend fun createGroup(
        authToken: String,
        request: CreateGroupRequest,
    ): CreateGroupResponse

    suspend fun getGroup(
        authToken: String,
        groupId: Identifier,
    ): GetGroupResponseDto

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

}