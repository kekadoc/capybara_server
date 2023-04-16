package com.kekadoc.project.capybara.server.routing.util.execution

import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.verifier.Verifier
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.ktor.server.routing.patch as ktorPatch

@KtorDsl
fun Route.patch(
    path: String,
    vararg verifiers: Verifier,
    body: PipelineInterceptor<Unit, ApplicationCall>,
): Route = patch(path, verifiers.toList(), body)

@KtorDsl
fun Route.patch(
    path: String,
    verifiers: List<Verifier> = emptyList(),
    body: PipelineInterceptor<Unit, ApplicationCall>,
): Route = ktorPatch(path) {
    execute(verifiers) {
        body.invoke(this, Unit)
    }
}

@KtorDsl
fun Route.patch(
    vararg verifiers: Verifier,
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route = patch(verifiers.toList(), body)

@KtorDsl
fun Route.patch(
    verifiers: List<Verifier> = emptyList(),
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route = ktorPatch {
    execute(verifiers) {
        body.invoke(this, Unit)
    }
}


@KtorDsl
@JvmName("patchTyped")
inline fun <reified R : Any> Route.patch(
    vararg verifiers: Verifier,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route = patch(verifiers = verifiers.toList(), body = body)

@KtorDsl
@JvmName("patchTyped")
inline fun <reified R : Any> Route.patch(
    verifiers: List<Verifier> = emptyList(),
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route = ktorPatch {
    execute(verifiers) {
        body(call.receive())
    }
}


@KtorDsl
@JvmName("patchTypedPath")
inline fun <reified R : Any> Route.patch(
    path: String,
    vararg verifiers: Verifier,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route = patch(path, verifiers = verifiers.toList(), body = body)

@KtorDsl
@JvmName("patchTypedPath")
inline fun <reified R : Any> Route.patch(
    path: String,
    verifiers: List<Verifier> = emptyList(),
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route = ktorPatch(path) {
    execute(verifiers) {
        body(call.receive())
    }
}