package com.kekadoc.project.capybara.server.routing.util

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.extensions.requireNotNull
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.user.User
import com.kekadoc.project.capybara.server.routing.errors.handleError
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import com.kekadoc.project.capybara.server.routing.verifier.Verifier
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
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

internal suspend inline fun <reified T : Any> PipelineContext.execute(
    vararg verifiers: Verifier,
    noinline block: suspend () -> T
) = execute(verifiers.toList(), block)

suspend fun PipelineContext.executeAuthorizedApi(
    vararg verifiers: Verifier,
    block: suspend () -> Unit
) = execute(listOf(ApiKeyVerifier, AuthorizationVerifier) + verifiers.toList(), block)

suspend inline fun <reified T : Any> PipelineContext.execute(
    verifiers: List<Verifier> = emptyList(),
    noinline block: suspend () -> T,
): Result<T> = kotlin.runCatching {
    verifiers.forEach { verifier -> verifier.verify(call) }
    block()
}
    .onSuccess { response ->
        call.respond(response)
    }
    .onFailure { error ->
        error.printStackTrace()
        call.handleError(error)
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

fun PipelineContext.requireQueryParameter(name: String): String {
    return call.request.queryParameters[name] ?: throw HttpException(HttpStatusCode.BadRequest, "Query parameter {$name} not found")
}


fun PipelineContext.requirePathId(): Identifier {
    return requireParameter("id").runCatching {
        UUID.fromString(this)
    }.getOrElse {
        throw HttpException(HttpStatusCode.BadRequest, "Parameter {id} not corrected")
    }
}

fun WebSocketServerSession.requirePathId(): Identifier = call.parameters["id"].requireNotNull().let(UUID::fromString)