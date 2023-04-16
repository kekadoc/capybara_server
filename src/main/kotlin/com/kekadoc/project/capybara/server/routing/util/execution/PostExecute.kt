package com.kekadoc.project.capybara.server.routing.util.execution

import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.verifier.Verifier
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.ktor.server.routing.post as ktorPost

@KtorDsl
fun Route.post(
    path: String,
    vararg verifiers: Verifier,
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route = post(path, verifiers.toList(), body)

@KtorDsl
fun Route.post(
    path: String,
    verifiers: List<Verifier> = emptyList(),
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route = ktorPost(path) {
    execute(verifiers) {
        body.invoke(this, Unit)
    }
}


@KtorDsl
fun Route.post(
    vararg verifiers: Verifier,
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route = post(verifiers.toList(), body)

@KtorDsl
fun Route.post(
    verifiers: List<Verifier> = emptyList(),
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route = ktorPost {
    execute(verifiers) {
        body.invoke(this, Unit)
    }
}


@KtorDsl
@JvmName("postTyped")
inline fun <reified R : Any> Route.post(
    vararg verifiers: Verifier,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route = post(verifiers.toList(), body)

@KtorDsl
@JvmName("postTyped")
inline fun <reified R : Any> Route.post(
    verifiers: List<Verifier> = emptyList(),
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route = ktorPost {
    execute(verifiers) {
        body(call.receive())
    }
}


@KtorDsl
@JvmName("postTypedPath")
inline fun <reified R : Any> Route.post(
    path: String,
    vararg verifiers: Verifier,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route = post(path, verifiers.toList(), body)

@KtorDsl
@JvmName("postTypedPath")
inline fun <reified R : Any> Route.post(
    path: String,
    verifiers: List<Verifier> = emptyList(),
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route = ktorPost(path) {
    execute(verifiers) {
        body(call.receive())
    }
}



