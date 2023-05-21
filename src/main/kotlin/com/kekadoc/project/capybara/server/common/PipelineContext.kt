package com.kekadoc.project.capybara.server.common

import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.application.*
import io.ktor.util.pipeline.PipelineContext as KtorPipelineContext

typealias PipelineContext = KtorPipelineContext<Unit, ApplicationCall>

val PipelineContext.authToken: Token
    get() = AuthorizationVerifier.requireAuthorizationToken()