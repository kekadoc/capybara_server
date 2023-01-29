package com.kekadoc.project.capybara.server.data.source.api_key

import com.google.firebase.database.FirebaseDatabase
import com.kekadoc.project.capybara.server.common.extensions.getAll
import com.kekadoc.project.capybara.server.common.extensions.remove
import com.kekadoc.project.capybara.server.common.extensions.set
import java.util.*

class ApiKeyDataSourceImpl(
    database: FirebaseDatabase
) : ApiKeyDataSource {
    
    private val apiKeys = database.getReference("/apiKeys")
    
    private val reservedApiKeys = setOf(
        "ff9de788-c243-4350-9ed0-7bfb847c4c1b",
        "43156ff2-0ba5-43a4-8cf7-54e9c14c373d",
        "26847e65-f3aa-4ae2-b9b1-6e1c8ec83737",
        "1fc0e461-859f-4fce-8840-9bf6c5687c67",
        "262135ad-6d22-4af5-95dc-622eb5d2e438",
        "7357c3db-1ff5-464a-86f6-cb2abd2caa24",
    )
    
    override suspend fun isApiKeyValid(apiKey: String): Result<Boolean> = runCatching {
        apiKeys.orderByValue().equalTo(apiKey).getAll<String>().isNotEmpty()
    }
    
    override suspend fun getNewApiKey(): Result<String> = kotlin.runCatching {
        var newApiKey = UUID.randomUUID().toString()
        while (apiKeys.orderByValue().equalTo(newApiKey).getAll<String>().isNotEmpty()) {
            newApiKey = UUID.randomUUID().toString()
        }
        apiKeys.push().set(newApiKey)
        newApiKey
    }
    
    override suspend fun saveApiKey(apiKey: String): Result<Unit> = kotlin.runCatching {
        removeApiKey(apiKey)
        apiKeys.push().set(apiKey)
    }
    
    override suspend fun removeApiKey(apiKey: String): Result<Unit> = kotlin.runCatching {
        val values = apiKeys.orderByValue().equalTo(apiKey).getAll<String>()
        values.forEach { (key, _) -> apiKeys.child(key).remove() }
    }
    
    override suspend fun reservedApiKeys(): Result<Set<String>> = kotlin.runCatching { reservedApiKeys }
    
}