package com.kekadoc.project.capybara.server.data.source.api.notification.mobile.remote

interface MobileNotificationsRemoteDataSource {

    suspend fun sendNotification(
        pushToken: String,
        title: String?,
        body: String,
        imageUrl: String?,
    ): String

}