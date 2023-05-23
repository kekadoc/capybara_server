package com.kekadoc.project.capybara.server.data.repository.api_key

import com.kekadoc.project.capybara.server.data.source.api.api_key.ApiKeyDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf

class ApiKeysRepositoryImpl(
    private val apiKeyDataSource: ApiKeyDataSource
) : ApiKeysRepository {
    
    override fun isApiKeyValid(apiKey: String): Flow<Boolean> = flowOf {
        apiKeyDataSource.isApiKeyValid(apiKey).getOrThrow()
    }.catch {
        val reserved = apiKeyDataSource.reservedApiKeys().getOrNull().orEmpty()
        emit(reserved.contains(apiKey))
    }
    
    override fun getNewApiKey(): Flow<String> = flowOf {
        apiKeyDataSource.getNewApiKey().getOrThrow()
    }
    
    override fun saveApiKey(apiKey: String): Flow<Unit> = flowOf {
        apiKeyDataSource.saveApiKey(apiKey).getOrThrow()
    }
    
    override fun removeApiKey(apiKey: String): Flow<Unit> = flowOf {
        apiKeyDataSource.removeApiKey(apiKey).getOrThrow()
    }
    
}