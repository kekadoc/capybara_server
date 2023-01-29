package com.kekadoc.project.capybara.server.data.repository.auth

import com.kekadoc.project.capybara.server.data.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    
    fun authorization(login: String): Flow<User>
    
}