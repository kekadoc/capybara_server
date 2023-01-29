package com.kekadoc.project.capybara.server.data.repository.auth

import com.kekadoc.project.capybara.server.common.Id
import com.kekadoc.project.capybara.server.common.Token
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.model.User
import com.kekadoc.project.capybara.server.data.source.user.UserDataSource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class)
class AuthRepositoryImpl(
    private val userDataSource: UserDataSource
) : AuthRepository {
    
    override fun authorization(login: String): Flow<User> {
        return userDataSource.getUserByLogin(login)
            .map { user -> user ?: generateEmptyUser(login) }
            .flatMapConcat {  user ->
                val authToken = Token.generate()
                val newUser = user.copy(authToken = authToken)
                userDataSource.setUser(
                    id = newUser.profile.id,
                    user = newUser
                ).map { newUser }
            }
    }
    
    private fun generateEmptyUser(login: String): User {
        return User(
            profile = Profile(
                id = Id.generate(),
                login = login
            )
        )
    }
    
}