package com.kekadoc.project.capybara.server.common.exception

import io.ktor.http.*

class HttpException(
    val statusCode: HttpStatusCode,
    message: String? = null
) : RuntimeException(message)