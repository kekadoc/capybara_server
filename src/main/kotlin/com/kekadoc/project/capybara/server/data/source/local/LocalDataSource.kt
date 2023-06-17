package com.kekadoc.project.capybara.server.data.source.local

interface LocalDataSource {

    suspend fun setDebugCreateMockData(value: Boolean)

    suspend fun getDebugCreateMockData(): Boolean

}