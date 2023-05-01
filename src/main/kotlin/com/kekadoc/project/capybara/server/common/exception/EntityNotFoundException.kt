package com.kekadoc.project.capybara.server.common.exception

import com.kekadoc.project.capybara.server.data.model.Identifier

open class EntityNotFoundException(
    message: String? = null,
    cause: Throwable? = null,
) : ServerException(message, cause)

class UserNotFound(
    val id: Identifier,
    throwable: Throwable? = null,
) : EntityNotFoundException(
    message = "User by id={$id} not found",
    cause = throwable,
)

class GroupNotFound(
    val id: Identifier,
    throwable: Throwable? = null,
) : EntityNotFoundException(
    message = "Group by id={$id} not found",
    cause = throwable,
)

class NotificationNotFound(
    val id: Identifier,
    throwable: Throwable? = null,
) : EntityNotFoundException(
    message = "Notification by id={$id} not found",
    cause = throwable,
)