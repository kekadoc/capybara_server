package com.kekadoc.project.capybara.server.data.source.user

import com.kekadoc.project.capybara.server.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    
    fun getUser(id: String): Flow<User?>
    
    fun getUserByToken(token: String): Flow<User?>
    
    fun getUserByLogin(login: String): Flow<User?>
    
    
    fun setUser(id: String, user: User): Flow<Unit>
    
    fun setUserByToken(token: String, user: User): Flow<Unit>
    
    
    fun deleteUser(id: String): Flow<Unit>
    
    fun deleteUserByToken(token: String): Flow<Unit>
}