package com.kekadoc.project.capybara.server.data.repository.user

import com.kekadoc.project.capybara.server.data.model.User
import com.kekadoc.project.capybara.server.data.source.user.UserDataSource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@OptIn(FlowPreview::class)
class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {
    
    override fun getUser(id: String): Flow<User?> {
        return userDataSource.getUser(id)
    }
    
    override fun getUserByToken(token: String): Flow<User?> {
        return userDataSource.getUserByToken(token)
    }
    
    override fun updateUser(id: String, user: User): Flow<Unit> {
        return userDataSource.setUser(id, user)
    }
    
    override fun updateUserByToken(token: String, user: User): Flow<Unit> {
        return userDataSource.setUserByToken(token, user)
    }
    
    override fun deleteUser(id: String): Flow<Unit> {
        return userDataSource.deleteUser(id)
    }
    
    override fun deleteUserByToken(token: String): Flow<Unit> {
        return userDataSource.deleteUserByToken(token)
    }
    
}