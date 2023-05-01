package com.kekadoc.project.capybara.server.data.repository.user

import com.kekadoc.project.capybara.server.data.model.Communications
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.model.User
import com.kekadoc.project.capybara.server.data.model.access.UserAccessToGroup
import com.kekadoc.project.capybara.server.data.model.access.UserAccessToUser
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun createUser(
        login: String,
        password: String,
        profile: Profile,
    ): Flow<User>

    fun deleteUser(id: Identifier): Flow<User?>

    fun getUsersByIds(ids: List<Identifier>): Flow<List<User>>

    fun getUserById(id: Identifier): Flow<User?>

    fun getUserByLogin(login: String): Flow<User?>

    fun updateUserPassword(
        userId: Identifier,
        newPassword: String,
    ): Flow<User?>

    fun updateUserProfile(
        userId: Identifier,
        profile: Profile,
    ): Flow<User?>

    fun updateUserCommunications(
        userId: Identifier,
        communications: Communications,
    ): Flow<User?>

    fun getAllAccessForUser(
        userId: Identifier,
    ): Flow<List<UserAccessToUser>>

    fun getAccessForUser(
        userId: Identifier,
        forUserId: Identifier,
    ): Flow<UserAccessToUser?>

    fun getAccessForUsers(
        userId: Identifier,
        forUserIds: List<Identifier>,
    ): Flow<List<UserAccessToUser>>

    fun updateAccessForUser(
        userId: Identifier,
        forUserId: Identifier,
        userAccessUser: UserAccessToUser.Updater,
    ): Flow<UserAccessToUser?>

    fun getAllAccessForGroup(
        userId: Identifier,
    ): Flow<List<UserAccessToGroup>>

    fun getAccessForGroup(
        userId: Identifier,
        groupId: Identifier,
    ): Flow<UserAccessToGroup?>

    fun getAccessForGroup(
        userId: Identifier,
        groupIds: List<Identifier>,
    ): Flow<List<UserAccessToGroup>>

    fun updateAccessForGroup(
        userId: Identifier,
        groupId: Identifier,
        userAccessGroup: UserAccessToGroup.Updater,
    ): Flow<UserAccessToGroup?>

}