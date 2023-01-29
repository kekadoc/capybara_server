package com.kekadoc.project.capybara.server.common

import io.ktor.server.application.*
import io.ktor.util.pipeline.PipelineContext

typealias PipelineContext = PipelineContext<Unit, ApplicationCall>