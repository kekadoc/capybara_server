package com.kekadoc.project.capybara.server.common.exception

class EntityNotFoundException(message: String? = null, cause: Throwable? = null) : ServerException(message, cause)