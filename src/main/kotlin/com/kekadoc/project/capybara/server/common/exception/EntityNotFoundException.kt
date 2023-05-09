package com.kekadoc.project.capybara.server.common.exception

import com.kekadoc.project.capybara.server.domain.model.Identifier

open class EntityNotFoundException(
    message: String? = null,
    cause: Throwable? = null,
) : ServerException(message, cause)

class UserNotFound(
    val id: Identifier? = null,
    val login: String? = null,
    throwable: Throwable? = null,
) : EntityNotFoundException(
    message = when {
        id != null -> "User by id={$id} not found"
        login != null -> "User by login={$login} not found"
        else -> "User not found"
    },
    cause = throwable,
)

class GroupNotFound(
    val id: Identifier,
    throwable: Throwable? = null,
) : EntityNotFoundException(
    message = "Group by id={$id} not found",
    cause = throwable,
)

class MessageNotFound(
    val id: Identifier,
    throwable: Throwable? = null,
) : EntityNotFoundException(
    message = "Notification by id={$id} not found",
    cause = throwable,
)

class ContactNotFound(
    val id: Identifier,
    throwable: Throwable? = null,
) : EntityNotFoundException(
    message = "Contact by id={$id} not found",
    cause = throwable,
)