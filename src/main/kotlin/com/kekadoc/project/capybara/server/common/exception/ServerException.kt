package com.kekadoc.project.capybara.server.common.exception

abstract class ServerException(
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)