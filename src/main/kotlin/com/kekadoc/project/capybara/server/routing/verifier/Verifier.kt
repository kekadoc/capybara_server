package com.kekadoc.project.capybara.server.routing.verifier

import io.ktor.server.application.*

interface Verifier {
    
    suspend fun verify(call: ApplicationCall)

}