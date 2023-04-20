package com.kekadoc.project.capybara.server.data.repository.user

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.user.Communications
import com.kekadoc.project.capybara.server.data.model.user.Person
import com.kekadoc.project.capybara.server.data.model.user.ProfileType
import com.kekadoc.project.capybara.server.data.model.user.User
import com.kekadoc.project.capybara.server.data.source.api.user.UsersDataSource
import kotlinx.coroutines.flow.Flow

class UsersRepositoryImpl(
    private val usersDataSource: UsersDataSource
) : UsersRepository {

    override fun createUser(login: String, person: Person, type: ProfileType): Flow<User> {
        return usersDataSource.createUser(login, person, type)
    }

    override fun getUserById(id: String): Flow<User?> {
        return usersDataSource.getUserById(id)
    }

    override fun getUserByToken(token: String): Flow<User?> {
        return usersDataSource.getUserByToken(token)
    }

    override fun getUserByLogin(login: String): Flow<User?> {
        return usersDataSource.getUserByLogin(login)
    }

    override fun updateUserPassword(id: String, password: String): Flow<User> {
        return usersDataSource.updateUserPassword(id, password)
    }

    override fun updateUserPersonal(id: String, person: Person): Flow<User> {
        return usersDataSource.updateUserPersonal(id, person)
    }

    override fun updateUserProfileType(id: String, type: ProfileType): Flow<User> {
        return usersDataSource.updateUserProfileType(id, type)
    }

    override fun updateUserPushToken(id: String, pushToken: String): Flow<User> {
        return usersDataSource.updateUserPushToken(id, pushToken)
    }

    override fun updateUserAuthToken(id: String, authToken: String): Flow<User> {
        return usersDataSource.updateUserAuthToken(id, authToken)
    }

    override fun updateUserCommunications(id: String, communications: Communications): Flow<User> {
        return usersDataSource.updateUserCommunications(id, communications)
    }

    override fun updateUserGroupAdd(id: String, groups: Set<Identifier>): Flow<User> {
        return usersDataSource.updateUserGroupAdd(id, groups)
    }

    override fun updateUserGroupRemove(id: String, groups: Set<Identifier>): Flow<User> {
        return usersDataSource.updateUserGroupRemove(id, groups)
    }

    override fun authorizeUser(login: String, password: String, pushToken: String): Flow<User> {
        return usersDataSource.authorizeUser(login, password, pushToken)
    }

    override fun deleteUser(id: String): Flow<Unit> {
        return usersDataSource.deleteUser(id)
    }

}