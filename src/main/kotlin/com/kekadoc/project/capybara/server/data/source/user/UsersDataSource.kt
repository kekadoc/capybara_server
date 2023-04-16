package com.kekadoc.project.capybara.server.data.source.user

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.user.Communications
import com.kekadoc.project.capybara.server.data.model.user.Person
import com.kekadoc.project.capybara.server.data.model.user.ProfileType
import com.kekadoc.project.capybara.server.data.model.user.User
import kotlinx.coroutines.flow.Flow

interface UsersDataSource {

    fun createUser(
        login: String,
        person: Person,
        type: ProfileType,
    ): Flow<User>

    fun getUserById(id: String): Flow<User?>

    fun getUserByToken(token: String): Flow<User?>

    fun getUserByLogin(login: String): Flow<User?>

    fun updateUserPassword(
        id: String,
        password: String,
    ): Flow<User>

    fun updateUserPersonal(
        id: String,
        person: Person,
    ): Flow<User>

    fun updateUserProfileType(
        id: String,
        type: ProfileType,
    ): Flow<User>

    fun updateUserPushToken(
        id: String,
        pushToken: String,
    ): Flow<User>

    fun updateUserAuthToken(
        id: String,
        authToken: String,
    ): Flow<User>

    fun updateUserGroupAdd(
        id: String,
        groups: Set<Identifier>,
    ): Flow<User>

    fun updateUserGroupRemove(
        id: String,
        groups: Set<Identifier>,
    ): Flow<User>

    fun updateUserCommunications(
        id: String,
        communications: Communications,
    ): Flow<User>

    fun authorizeUser(
        login: String,
        password: String,
        pushToken: String,
    ): Flow<User>

    fun deleteUser(id: String): Flow<Unit>

}