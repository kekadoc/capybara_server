package com.kekadoc.project.capybara.server.data.repository.user

import com.kekadoc.project.capybara.server.data.source.api.user.UsersDataSource
import com.kekadoc.project.capybara.server.data.source.api.user.access.UserAccessDataSource
import com.kekadoc.project.capybara.server.data.source.api.user.communication.UserCommunicationsDataSource
import com.kekadoc.project.capybara.server.domain.model.*
import com.kekadoc.project.capybara.server.domain.model.common.Range
import com.kekadoc.project.capybara.server.domain.model.user.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UsersRepositoryImpl(
    private val usersDataSource: UsersDataSource,
    private val userCommunicationsDataSource: UserCommunicationsDataSource,
    private val userAccessDataSource: UserAccessDataSource,
) : UsersRepository {

    override fun createUser(
        login: String,
        password: String,
        profile: Profile,
    ): Flow<User> = flowOf {
        usersDataSource.createUser(
            login = login,
            password = password,
            profile = profile,
        )
    }

    override fun deleteUser(id: Identifier): Flow<User> = flowOf {
        usersDataSource.deleteUser(id)
    }

    override fun getUserById(id: Identifier): Flow<User> = flowOf {
        usersDataSource.getUserById(id)
    }

    override fun getUsersByIds(ids: List<Identifier>): Flow<List<User>> = flowOf {
        usersDataSource.getUsersByIds(ids)
    }

    override fun getUsers(range: Range): Flow<List<User>> = flowOf {
        usersDataSource.getUsers(range)
    }

    override fun findUserByLogin(login: String): Flow<User?> = flowOf {
        usersDataSource.findUserByLogin(login)
    }

    override fun updateUserStatus(userId: Identifier, status: UserStatus): Flow<User> = flowOf {
        usersDataSource.updateUserStatus(
            userId = userId,
            status = status,
        )
    }

    override fun updateUserPassword(
        userId: Identifier,
        newPassword: String,
    ): Flow<User> = flowOf {
        usersDataSource.updateUserPassword(
            userId = userId,
            newPassword = newPassword,
        )
    }

    override fun updateUserProfile(
        userId: Identifier,
        profile: Profile,
    ): Flow<User> = flowOf {
        usersDataSource.updateUserProfile(
            userId = userId,
            profile = profile,
        )
    }

    override fun updateUserCommunications(
        userId: Identifier,
        communications: Communications,
    ): Flow<User> = flowOf {
        userCommunicationsDataSource.updateUserCommunications(
            userId = userId,
            communications = communications,
        )
    }

    override fun getAllAccessForUser(userId: Identifier): Flow<List<UserAccessToUser>> = flowOf {
        userAccessDataSource.getAllAccessForUser(
            userId = userId,
        )
    }

    override fun getAccessForUser(
        userId: Identifier,
        forUserId: Identifier,
    ): Flow<UserAccessToUser> = flowOf {
        userAccessDataSource.getAccessForUser(userId, forUserId)
    }

    override fun getAccessForUsers(
        userId: Identifier,
        forUserIds: List<Identifier>,
    ): Flow<List<UserAccessToUser>> = flowOf {
        userAccessDataSource.getAccessForUsers(
            userId = userId,
            forUserIds = forUserIds,
        )
    }

    override fun updateAccessForUser(
        userId: Identifier,
        forUserId: Identifier,
        userAccessUser: UserAccessToUser.Updater,
    ): Flow<UserAccessToUser> = flowOf {
        userAccessDataSource.updateAccessForUser(
            userId = userId,
            forUserId = forUserId,
            userAccessUser = userAccessUser,
        )
    }

    override fun getAllAccessForGroup(userId: Identifier): Flow<List<UserAccessToGroup>> = flowOf {
        userAccessDataSource.getAllAccessForGroup(
            userId = userId,
        )
    }

    override fun getAccessForGroup(
        userId: Identifier,
        groupId: Identifier,
    ): Flow<UserAccessToGroup> = flowOf {
        userAccessDataSource.getAccessForGroup(
            userId = userId,
            groupId = groupId,
        )
    }

    override fun getAccessForGroup(
        userId: Identifier,
        groupIds: List<Identifier>,
    ): Flow<List<UserAccessToGroup>> = flowOf {
        userAccessDataSource.getAccessForGroup(
            userId = userId,
            groupIds = groupIds,
        )
    }

    override fun updateAccessForGroup(
        userId: Identifier,
        groupId: Identifier,
        userAccessGroup: UserAccessToGroup.Updater,
    ): Flow<UserAccessToGroup> = flowOf {
        userAccessDataSource.updateAccessForGroup(
            userId = userId,
            groupId = groupId,
            userAccessGroup = userAccessGroup
        )
    }

}