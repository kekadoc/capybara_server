package com.kekadoc.project.capybara.server.data.source.api.notification

import kotlinx.coroutines.flow.Flow

interface MobileNotificationsDataSource {

    fun sendNotification(
        pushToken: String,
        title: String,
        body: String,
        imageUrl: String,
    ): Flow<String>

}