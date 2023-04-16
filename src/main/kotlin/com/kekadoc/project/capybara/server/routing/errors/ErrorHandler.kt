@file:OptIn(InternalAPI::class, ExperimentalSerializationApi::class)

package com.kekadoc.project.capybara.server.routing.errors

import com.kekadoc.project.capybara.server.common.exception.HttpException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException

internal suspend fun ApplicationCall.handleError(e: Throwable) {
    when (e) {
        is HttpException -> {
            respond(e.statusCode, e.message.orEmpty())
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
            respond(HttpStatusCode.InternalServerError, e.localizedMessage)
        }
    }
}