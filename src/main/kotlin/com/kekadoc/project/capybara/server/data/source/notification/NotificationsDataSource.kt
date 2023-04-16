package com.kekadoc.project.capybara.server.data.source.notification

import kotlinx.coroutines.flow.Flow

interface NotificationsDataSource {

    fun sendNotification(
        pushToken: String,
        title: String,
        body: String,
        imageUrl: String,
    ): Flow<String>

}