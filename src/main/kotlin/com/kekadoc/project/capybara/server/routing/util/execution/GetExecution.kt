package com.kekadoc.project.capybara.server.routing.util.execution

import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.verifier.Verifier
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.ktor.server.routing.get as ktorGet

@KtorDsl
fun Route.get(
    path: String,
    vararg verifiers: Verifier,
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route = get(path, verifiers.toList(), body)

@KtorDsl
fun Route.get(
    path: String,
    verifiers: List<Verifier> = emptyList(),
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route = ktorGet(path) {
    execute(verifiers) {
        body.invoke(this, Unit)
    }
}

@KtorDsl
fun Route.get(
    vararg verifiers: Verifier,
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route = get(verifiers.toList(), body)

@KtorDsl
fun Route.get(
    verifiers: List<Verifier> = emptyList(),
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route = ktorGet {
    execute(verifiers) {
        body.invoke(this, Unit)
    }
}