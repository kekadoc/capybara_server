package com.kekadoc.project.capybara.server.routing.api.auth.errors

import com.kekadoc.project.capybara.server.common.exception.ServerException

class UserNotRegisteredException(message: String? = null, cause: Throwable? = null) : ServerException(message, cause)