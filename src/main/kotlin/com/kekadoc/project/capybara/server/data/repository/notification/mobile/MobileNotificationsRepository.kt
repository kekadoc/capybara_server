package com.kekadoc.project.capybara.server.data.repository.notification.mobile

import kotlinx.coroutines.flow.Flow

interface MobileNotificationsRepository {

    fun setUserPushToken(
        userId: String,
        pushToken: String,
    ): Flow<Unit>

    fun getUserPushToken(
        userId: String,
    ): Flow<String>

    fun deleteUserPushToken(
        userId: String,
    ): Flow<Unit>

}