package com.kekadoc.project.capybara.server.data.source.local

import java.util.prefs.Preferences

class LocalDataSourceImpl : LocalDataSource {

    override suspend fun setDebugCreateMockData(value: Boolean) {
        Preferences.userRoot()
            .node("debug")
            .putBoolean("isCreateMockData", value)
    }

    override suspend fun getDebugCreateMockData(): Boolean {
        return Preferences.userRoot()
            .node("debug")
            .getBoolean("isCreateMockData", false)
    }

}