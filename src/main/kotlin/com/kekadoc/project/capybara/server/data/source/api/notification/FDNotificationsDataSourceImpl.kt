package com.kekadoc.project.capybara.server.data.source.api.notification

import com.google.firebase.database.FirebaseDatabase
import com.kekadoc.project.capybara.server.common.extensions.flowOf
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.model.NotificationInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class FDNotificationsDataSourceImpl(
    database: FirebaseDatabase,
) : NotificationsDataSource {

//    private val messages = database.getReference("/messages")
//
//    override fun getAll(): Flow<List<Notification>> = flowOf {
//        throw NotImplementedError()
////        messages
////            .getAll<Message>()
////            .values.toList()
////            .filterNotNull()
//    }
//
//    override fun createNotification(
//        authorId: Identifier,
//        type: Notification.Type,
//        addresseeGroups: Set<Identifier>,
//        addresseeUsers: Set<Identifier>,
//        content: Notification.Content
//    ): Flow<Notification> {
//        return flow {
//            throw NotImplementedError()
////            val newMessageDocument = messages.push()
////            val message = Message(
////                id = newMessageDocument.key,
////                authorId = authorId,
////                addresseeGroups = addresseeGroups.toList(),
////                addresseeUsers = addresseeUsers.toList(),
////                content = content,
////                state = Message.State(
////                    status = Message.State.Status.SENT, // TODO:
////                )
////            )
////            newMessageDocument.set(message)
////            emit(message)
//        }
//    }
//
//    override fun updateNotification(
//        messageId: Identifier,
//        content: Notification.Content,
//    ): Flow<Notification> = flowOf {
//        throw NotImplementedError()
////        return getMessage(messageId)
////            .map { message -> message ?: throw IllegalStateException("Message not found") }
////            .map { currentMessage ->
////                currentMessage.copy(
////                    content = content,
////                )
////            }
////            .onEach { newMessage -> messages.child(messageId).set(newMessage) }
//    }
//
//    override fun updateNotificationStatus(
//        messageId: Identifier,
//        status: NotificationInfo.Status
//    ): Flow<Notification> = flowOf {
//        TODO("Not yet implemented")
////        return getMessage(messageId)
////            .map { message -> message ?: throw IllegalStateException("Message not found") }
////            .map { currentMessage ->
////                currentMessage.copy(
////                    state = state,
////                )
////            }
////            .onEach { newMessage -> messages.child(messageId).set(newMessage) }
//    }
//
//    override fun updateNotificationUserInfo(
//        messageId: Identifier,
//        info: NotificationInfo.FromUserInfo,
//    ): Flow<Notification> = flow {
//        TODO("Not yet implemented")
//    }
//
//    override fun removeNotification(messageId: Identifier): Flow<Unit> {
//        TODO("Not yet implemented")
//    }
//
////    override fun removeNotification(
////        messageId: String,
////    ): Flow<Unit> {
////        return flowOf {
////            messages.child(messageId).remove()
////        }
////    }
//
//    override fun getNotification(
//        messageId: Identifier,
//    ): Flow<Notification?> {
//        return flow {
//            throw NotImplementedError()
////            val message = messages.child(messageId).get<Message?>()
////            emit(message)
//        }
//    }
//
//    override fun getNotificationsByAuthorId(
//        authorId: Identifier,
//    ): Flow<List<Notification>> = flow {
////        throw NotImplementedError()
////        return flowOf {
////            messages
////                .orderByChild("authorId")
////                .equalTo(authorId)
////                .getAll<Message>()
////                .values
////                .filterNotNull()
////                .toList()
////        }
//    }
//
//    override fun getNotificationByAddresseeUserId(userId: Identifier): Flow<List<Notification>> = flowOf {
//        throw NotImplementedError()
////        messages
////            .getAll<Message>()
////            .values.toList()
////            .filterNotNull()
////            .filter { message ->
////                message.addresseeUsers.any { userId ->
////                    userId == userId
////                }
////            }
//    }
//
//    override fun getNotificationByAddresseeGroupIds(ids: Set<Identifier>): Flow<List<Notification>> = flowOf {
//        throw NotImplementedError()
////        messages
////            .getAll<Message>()
////            .values.toList()
////            .filterNotNull()
////            .filter { message ->
////                message.addresseeGroups.any { userId ->
////                    ids.any { it == userId }
////                }
////            }
//    }
//
}