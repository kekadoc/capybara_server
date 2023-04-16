package com.kekadoc.project.capybara.server.intercator.groups

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.routing.api.groups.model.*

interface GroupsInteractor {

    suspend fun createGroup(
        authToken: String,
        request: CreateGroupRequest,
    ): CreateGroupResponse

    suspend fun getGroup(
        authToken: String,
        groupId: Identifier,
    ): GetGroupResponse

    suspend fun updateGroup(
        authToken: String,
        groupId: Identifier,
        request: UpdateGroupRequest,
    ): UpdateGroupResponse

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