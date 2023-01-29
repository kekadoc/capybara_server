package com.kekadoc.project.capybara.server.routing.api.v1.auth

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.repository.auth.AuthRepository
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.routing.execute
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import org.koin.core.component.get

@Serializable
data class AuthorizationRequest(
    val login: String
)

@Serializable
data class AuthorizationResponse(
    val token: String,
    val profile: Profile
)

suspend fun PipelineContext.authorization() = execute {
    val request = this.call.receive<AuthorizationRequest>()
    
    val userRepository = Di.get<AuthRepository>()
    val user = userRepository.authorization(request.login).first()
    
    val response = AuthorizationResponse(
        token = user.authToken,
        profile = user.profile
    )
    call.respond(response)
}