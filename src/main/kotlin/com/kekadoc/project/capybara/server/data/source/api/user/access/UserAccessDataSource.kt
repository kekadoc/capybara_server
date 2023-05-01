package com.kekadoc.project.capybara.server.data.source.api.user.access

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.access.UserAccessToGroup
import com.kekadoc.project.capybara.server.data.model.access.UserAccessToUser
import kotlinx.coroutines.flow.Flow

interface UserAccessDataSource {

    suspend fun getAllAccessForUser(
        userId: Identifier,
    ): List<UserAccessToUser>

    suspend fun getAccessForUser(
        userId: Identifier,
        forUserId: Identifier,
    ): UserAccessToUser?

    fun getAccessForUsers(
        userId: Identifier,
        forUserIds: List<Identifier>,
    ): List<UserAccessToUser>

    suspend fun updateAccessForUser(
        userId: Identifier,
        forUserId: Identifier,
        userAccessUser: UserAccessToUser.Updater,
    ): UserAccessToUser?

    suspend fun getAllAccessForGroup(
        userId: Identifier,
    ): List<UserAccessToGroup>

    suspend fun getAccessForGroup(
        userId: Identifier,
        groupId: Identifier,
    ): UserAccessToGroup?

    suspend fun getAccessForGroup(
        userId: Identifier,
        groupIds: List<Identifier>,
    ): List<UserAccessToGroup>

    suspend fun updateAccessForGroup(
        userId: Identifier,
        groupId: Identifier,
        userAccessGroup: UserAccessToGroup.Updater,
    ): UserAccessToGroup?

}