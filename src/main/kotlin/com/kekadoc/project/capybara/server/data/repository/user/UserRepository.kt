package com.kekadoc.project.capybara.server.data.repository.user

import com.kekadoc.project.capybara.server.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    
    fun getUser(id: String): Flow<User?>
    
    fun getUserByToken(token: String): Flow<User?>
    
    
    fun updateUser(id: String, user: User): Flow<Unit>
    
    fun updateUserByToken(token: String, user: User): Flow<Unit>
    
    
    fun deleteUser(id: String): Flow<Unit>
    
    fun deleteUserByToken(token: String): Flow<Unit>
    
}