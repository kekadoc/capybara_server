package com.kekadoc.project.capybara.server.data.source.api.user

import com.kekadoc.project.capybara.server.domain.model.*

interface UsersDataSource {

    suspend fun createUser(
        login: String,
        password: String,
        profile: Profile,
    ): User

    suspend fun deleteUser(id: Identifier): User

    suspend fun getUserById(id: Identifier): User

    suspend fun getUsersByIds(ids: List<Identifier>): List<User>

    suspend fun findUsersByIds(ids: List<Identifier>): List<User>

    suspend fun findUserByLogin(login: String): User?

    suspend fun getUsers(range: Range): List<User>

    suspend fun updateUserStatus(
        userId: Identifier,
        status: UserStatus,
    ): User

    suspend fun updateUserPassword(
        userId: Identifier,
        newPassword: String,
    ): User

    suspend fun updateUserProfile(
        userId: Identifier,
        profile: Profile,
    ): User

}