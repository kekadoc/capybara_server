package com.kekadoc.project.capybara.server.routing.util

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.extensions.requireNotNull
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.User
import com.kekadoc.project.capybara.server.routing.errors.handleError
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import com.kekadoc.project.capybara.server.routing.verifier.Verifier
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import java.util.*

interface AuthorizedScope {
    val user: User
}

interface RequestBodyScope<Request> {
    val request: Request
}

interface RespondBodyScope<Response> {

    suspend fun respond(
        status: HttpStatusCode = HttpStatusCode.OK,
    )

    suspend fun respond(
        status: HttpStatusCode = HttpStatusCode.OK,
        block: suspend () -> Response
    )

}

interface AuthorizedExecutionScope<Request, Response> : AuthorizedScope, RequestBodyScope<Request>, RespondBodyScope<Response>

suspend fun <T> PipelineContext.execute(
    vararg verifiers: Verifier,
    block: suspend () -> T
) = execute(verifiers.toList(), block)

suspend fun PipelineContext.executeAuthorizedApi(
    vararg verifiers: Verifier,
    block: suspend () -> Unit
) = execute(listOf(ApiKeyVerifier, AuthorizationVerifier) + verifiers.toList(), block)

suspend fun <T> PipelineContext.execute(
    verifiers: List<Verifier> = emptyList(),
    block: suspend () -> T
) {
    try {
        verifiers.forEach { verifier -> verifier.verify(call) }
        block()
    } catch (e: Throwable) {
        println("____ERROR $e")
        call.handleError(e)
    }
}



suspend fun WebSocketServerSession.execute(
    verifiers: List<Verifier> = emptyList(),
    block: suspend () -> Unit
) {

    try {
        verifiers.forEach { verifier -> verifier.verify(call) }
        block()
    } catch (e: Throwable) {
        call.handleError(e)
    }
}

suspend fun WebSocketServerSession.execute(
    vararg verifiers: Verifier,
    block: suspend () -> Unit
) {

    try {
        verifiers.forEach { verifier -> verifier.verify(call) }
        block()
    } catch (e: Throwable) {
        call.handleError(e)
    }
}

suspend fun WebSocketServerSession.executeAuthorizedApi(
    vararg verifiers: Verifier,
    block: suspend () -> Unit
) = execute(listOf(ApiKeyVerifier, AuthorizationVerifier) + verifiers.toList(), block)




fun PipelineContext.requireParameter(name: String): String {
    return call.parameters[name] ?: throw HttpException(HttpStatusCode.BadRequest, "Parameter {$name} not found")
}

fun PipelineContext.requirePathId(): Identifier {
    return requireParameter("id").runCatching {
        UUID.fromString(this)
    }.getOrElse {
        throw HttpException(HttpStatusCode.BadRequest, "Parameter {id} not corrected")
    }
}

fun WebSocketServerSession.requirePathId(): Identifier = call.parameters["id"].requireNotNull().let(UUID::fromString)