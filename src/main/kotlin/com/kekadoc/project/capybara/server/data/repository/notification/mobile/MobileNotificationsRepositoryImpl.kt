package com.kekadoc.project.capybara.server.data.repository.notification.mobile

import kotlinx.coroutines.flow.Flow

class MobileNotificationsRepositoryImpl : MobileNotificationsRepository {

    override fun setUserPushToken(userId: String, pushToken: String): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun getUserPushToken(userId: String): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun deleteUserPushToken(userId: String): Flow<Unit> {
        TODO("Not yet implemented")
    }

}