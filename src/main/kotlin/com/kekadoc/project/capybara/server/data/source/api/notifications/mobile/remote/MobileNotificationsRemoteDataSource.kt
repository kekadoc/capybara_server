package com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.remote

import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.user.User

interface MobileNotificationsRemoteDataSource {

    suspend fun sendNotification(
        pushToken: String,
        message: Message,
        author: User,
    ): String

}