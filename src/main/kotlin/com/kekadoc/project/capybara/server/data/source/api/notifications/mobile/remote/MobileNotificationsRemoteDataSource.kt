package com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.remote

import com.kekadoc.project.capybara.server.domain.model.message.MessageAction

interface MobileNotificationsRemoteDataSource {

    suspend fun sendNotification(
        pushToken: String,
        title: String?,
        body: String,
        imageUrl: String?,
        actions: List<MessageAction>,
    ): String

}