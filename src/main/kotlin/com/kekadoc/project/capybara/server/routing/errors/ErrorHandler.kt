@file:OptIn(InternalAPI::class, ExperimentalSerializationApi::class)

package com.kekadoc.project.capybara.server.routing.errors

import com.kekadoc.project.capybara.server.common.exception.EntityNotFoundException
import com.kekadoc.project.capybara.server.common.exception.HttpException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException

suspend fun ApplicationCall.handleError(e: Throwable) {
    e.printStackTrace()
    when (e) {
        is HttpException -> {
            respond(e.statusCode, e.message.orEmpty())
        }
        is EntityNotFoundException -> {
            respond(
                HttpStatusCode.NotFound,
                e.message.orEmpty(),
            )
        }
        is BadRequestException -> {
            when (val root = e.rootCause ?: e) {
                is MissingFieldException -> {
                    val missingFields = root.missingFields.joinToString(prefix = "[", postfix = "]")
                    respond(HttpStatusCode.BadRequest, "Missing fields: $missingFields")
                }
                else -> respond(HttpStatusCode.BadRequest, (e.rootCause ?: e).localizedMessage)
            }
        }
        else -> {
            val message = buildString {
                var error: Throwable? = e
                while (error != null) {
                    append(error.localizedMessage)
                    error = error.cause
                    if (error != null) {
                        appendLine()
                    }
                }
            }
            respond(
                HttpStatusCode.InternalServerError,
                message,
            )
        }
    }
}