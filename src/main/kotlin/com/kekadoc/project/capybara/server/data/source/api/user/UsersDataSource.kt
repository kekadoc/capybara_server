package com.kekadoc.project.capybara.server.data.source.api.user

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.model.User

interface UsersDataSource {

    suspend fun createUser(
        login: String,
        password: String,
        profile: Profile,
    ): User

    suspend fun deleteUser(id: Identifier): User?

    suspend fun getUserById(id: Identifier): User?

    suspend fun getUsersByIds(ids: List<Identifier>): List<User>

    suspend fun getUserByLogin(login: String): User?

    suspend fun updateUserPassword(
        userId: Identifier,
        newPassword: String,
    ): User?

    suspend fun updateUserProfile(
        userId: Identifier,
        profile: Profile,
    ): User?

}