package com.kekadoc.project.capybara.server.data.repository.user

import com.kekadoc.project.capybara.server.data.model.Communications
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.model.User
import com.kekadoc.project.capybara.server.data.source.api.user.UsersDataSource
import kotlinx.coroutines.flow.Flow

class UsersRepositoryImpl(
    private val usersDataSource: UsersDataSource,
) : UsersRepository {

    override fun createUser(login: String, profile: Profile): Flow<User> {
        return usersDataSource.createUser(login, profile)
    }

    override fun deleteUser(id: String): Flow<Unit> {
        return usersDataSource.deleteUser(id)
    }

    override fun getUserById(id: String): Flow<User?> {
        return usersDataSource.getUserById(id)
    }

    override fun getUsersByIds(ids: List<Identifier>): Flow<List<User>> {
        return usersDataSource.getUsersByIds(ids)
    }

    override fun getUserByToken(token: String): Flow<User?> {
        return usersDataSource.getUserByToken(token)
    }

    override fun getUserByLogin(login: String): Flow<User?> {
        return usersDataSource.getUserByLogin(login)
    }

    override fun updateUserPassword(userId: Identifier, password: String): Flow<User> {
        return usersDataSource.updateUserPassword(userId, password)
    }

    override fun updateUserProfile(userId: Identifier, profile: Profile): Flow<User> {
        return usersDataSource.updateUserProfile(userId, profile)
    }

    override fun updateUserCommunications(
        userId: Identifier,
        communications: Communications,
    ): Flow<User> {
        return usersDataSource.updateUserCommunications(userId, communications)
    }

    override fun updateUserAvailabilityGroupsAdd(
        userId: Identifier,
        groupIds: Set<Identifier>,
    ): Flow<User> {
        return usersDataSource.updateUserAvailabilityGroupsAdd(
            userId = userId,
            groupIds = groupIds,
        )
    }

    override fun updateUserAvailabilityGroupsRemove(
        userId: Identifier,
        groupIds: Set<Identifier>,
    ): Flow<User> {
        return usersDataSource.updateUserAvailabilityGroupsRemove(
            userId = userId,
            groupIds = groupIds,
        )
    }

    override fun updateUserAvailabilityUsersAdd(
        userId: Identifier,
        userIds: Set<Identifier>,
    ): Flow<User> {
        return usersDataSource.updateUserAvailabilityUsersAdd(
            userId = userId,
            userIds = userIds,
        )
    }

    override fun updateUserAvailabilityUsersRemove(
        userId: Identifier,
        userIds: Set<Identifier>,
    ): Flow<User> {
        return usersDataSource.updateUserAvailabilityUsersRemove(
            userId = userId,
            userIds = userIds,
        )
    }

    override fun updateUserAvailabilityContactsAdd(
        userId: Identifier,
        contactIds: Set<Identifier>,
    ): Flow<User> {
        return usersDataSource.updateUserAvailabilityContactsAdd(
            userId = userId,
            contactIds = contactIds,
        )
    }

    override fun updateUserAvailabilityContactsRemove(
        userId: Identifier,
        contactIds: Set<Identifier>,
    ): Flow<User> {
        return usersDataSource.updateUserAvailabilityContactsRemove(
            userId = userId,
            contactIds = contactIds,
        )
    }

}