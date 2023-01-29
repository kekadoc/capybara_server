package com.kekadoc.project.capybara.server.data.repository.notification

import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    
    fun sendNotification(userId: String, title: String, body: String, imageUrl: String): Flow<String>
    
    fun savePushToken(userId: String, pushToken: String): Flow<Unit>
    
}