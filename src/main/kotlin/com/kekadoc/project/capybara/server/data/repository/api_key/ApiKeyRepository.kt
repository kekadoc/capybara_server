package com.kekadoc.project.capybara.server.data.repository.api_key

import kotlinx.coroutines.flow.Flow

interface ApiKeyRepository {
    
    fun isApiKeyValid(apiKey: String): Flow<Boolean>
    
    fun getNewApiKey(): Flow<String>
    
    fun saveApiKey(apiKey: String): Flow<Unit>
    
    fun removeApiKey(apiKey: String): Flow<Unit>
    
}