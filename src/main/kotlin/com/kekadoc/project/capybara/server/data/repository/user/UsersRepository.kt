package com.kekadoc.project.capybara.server.data.repository.user

import com.kekadoc.project.capybara.server.data.model.Communications
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun createUser(
        login: String,
        profile: Profile,
    ): Flow<User>

    fun deleteUser(id: String): Flow<Unit>

    fun getUserById(id: String): Flow<User?>

    fun getUsersByIds(ids: List<Identifier>): Flow<List<User>>

    fun getUserByToken(token: String): Flow<User?>

    fun getUserByLogin(login: String): Flow<User?>

    fun updateUserPassword(
        userId: Identifier,
        password: String,
    ): Flow<User>

    fun updateUserProfile(
        userId: Identifier,
        profile: Profile,
    ): Flow<User>

    fun updateUserCommunications(
        userId: Identifier,
        communications: Communications,
    ): Flow<User>

    fun updateUserAvailabilityGroupsAdd(
        userId: Identifier,
        groupIds: Set<Identifier>,
    ): Flow<User>

    fun updateUserAvailabilityGroupsRemove(
        userId: Identifier,
        groupIds: Set<Identifier>,
    ): Flow<User>

    fun updateUserAvailabilityUsersAdd(
        userId: Identifier,
        userIds: Set<Identifier>,
    ): Flow<User>

    fun updateUserAvailabilityUsersRemove(
        userId: Identifier,
        userIds: Set<Identifier>,
    ): Flow<User>

    fun updateUserAvailabilityContactsAdd(
        userId: Identifier,
        contactIds: Set<Identifier>,
    ): Flow<User>

    fun updateUserAvailabilityContactsRemove(
        userId: Identifier,
        contactIds: Set<Identifier>,
    ): Flow<User>

}