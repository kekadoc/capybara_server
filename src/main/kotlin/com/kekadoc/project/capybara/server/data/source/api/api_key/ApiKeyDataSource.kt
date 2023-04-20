package com.kekadoc.project.capybara.server.data.source.api.api_key

interface ApiKeyDataSource {
    
    suspend fun isApiKeyValid(apiKey: String): Result<Boolean>
    
    suspend fun getNewApiKey(): Result<String>
    
    suspend fun saveApiKey(apiKey: String): Result<Unit>
    
    suspend fun removeApiKey(apiKey: String): Result<Unit>
    
    suspend fun reservedApiKeys(): Result<Set<String>>
    
}