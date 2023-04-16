package com.kekadoc.project.capybara.server.routing.util.execution

import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.verifier.Verifier
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.ktor.server.routing.delete as ktorDelete

@KtorDsl
fun Route.delete(
    path: String,
    vararg verifiers: Verifier,
    body: PipelineInterceptor<Unit, ApplicationCall>,
): Route = delete(path, verifiers.toList(), body)

@KtorDsl
fun Route.delete(
    path: String,
    verifiers: List<Verifier> = emptyList(),
    body: PipelineInterceptor<Unit, ApplicationCall>,
): Route = ktorDelete(path) {
    execute(verifiers) {
        body.invoke(this, Unit)
    }
}

@KtorDsl
fun Route.delete(
    vararg verifiers: Verifier,
    body: PipelineInterceptor<Unit, ApplicationCall>,
): Route = delete(verifiers.toList(), body)

@KtorDsl
fun Route.delete(
    verifiers: List<Verifier> = emptyList(),
    body: PipelineInterceptor<Unit, ApplicationCall>,
): Route = ktorDelete {
    execute(verifiers) {
        body.invoke(this, Unit)
    }
}
