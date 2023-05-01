package com.kekadoc.project.capybara.server.intercator

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.model.User
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun Flow<Notification>.requireAuthor(user: User): Flow<Notification> {
    return onEach { message ->
        if (message.authorId != user.id) {
            throw HttpException(HttpStatusCode.Forbidden)
        }
    }
}

fun Flow<User?>.requireUser(): Flow<User> = map { user ->
    user ?: throw HttpException(HttpStatusCode.NotFound)
}

fun Flow<User?>.requireAuthorizedUser(): Flow<User> = map { user ->
    user ?: throw HttpException(HttpStatusCode.Unauthorized)
}

fun Flow<User>.requireAdminUser(): Flow<User> = onEach { user ->
    if (user.profile.type != Profile.Type.ADMIN)
        throw HttpException(HttpStatusCode.Forbidden)
}

fun Flow<User>.requireSpeakerUser(): Flow<User> = onEach { user ->
    if (user.profile.type != Profile.Type.SPEAKER)
        throw HttpException(HttpStatusCode.Forbidden)
}

fun Flow<Notification?>.orNotFoundError(): Flow<Notification> = map { message ->
    message ?: throw HttpException(HttpStatusCode.NotFound)
}

fun Flow<Notification>.requireAddressee(user: User): Flow<Notification> = onEach { notification ->
    if (!notification.addresseeUserIds.contains(user.id) && !notification.addresseeGroupIds.any { user.groupIds.contains(it) }) {
        throw HttpException(
            statusCode = HttpStatusCode.Forbidden,
            message = "The message is only available to the recipient ${notification.addresseeUserIds}",
        )
    }
}