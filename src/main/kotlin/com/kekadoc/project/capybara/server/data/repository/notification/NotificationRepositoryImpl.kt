package com.kekadoc.project.capybara.server.data.repository.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.source.user.UserDataSource
import io.ktor.http.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

//"dmFDnkJDTvOMh1QQ2PkfQN:APA91bFTbxX4Kg9bn8KR0nfUPEfKojPKjTFBaeY6g_VQdmbtkTMROyCnK_5HoqkmlWdNMuPTqyo7MYTv3xnSl6DxsYSHiESyE2JfcxAEW3QTeER48COINsQDKlgoEtxsrwOBRsZ2-fX-"
@OptIn(FlowPreview::class)
class NotificationRepositoryImpl(
    private val userDataSource: UserDataSource
) : NotificationRepository {
    
    override fun sendNotification(userId: String, title: String, body: String, imageUrl: String): Flow<String> {
        return userDataSource.getUser(userId).map { user ->
            user ?: throw HttpException(HttpStatusCode.NotFound)
            user.pushToken
        }.map { pushToken ->
            val notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .setImage(imageUrl)
                .build()
            val message = Message.builder()
                .setNotification(notification)
                .setToken(pushToken)
                .build()
            FirebaseMessaging.getInstance().send(message)
        }
    }
    
    override fun savePushToken(userId: String, pushToken: String): Flow<Unit> {
        return userDataSource.getUser(userId).flatMapConcat { user ->
            user ?: throw HttpException(HttpStatusCode.NotFound)
            val newUser = user.copy(pushToken = pushToken)
            userDataSource.setUser(userId, newUser)
        }
    }
    
}