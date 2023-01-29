package com.kekadoc.project.capybara.server.routing.api.v1.profile

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.extensions.requireNotNull
import com.kekadoc.project.capybara.server.data.model.Organization
import com.kekadoc.project.capybara.server.data.model.Person
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.model.ProfileType
import com.kekadoc.project.capybara.server.data.repository.user.UserRepository
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.routing.execute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import org.koin.core.component.get

fun PipelineContext.getAuthorizationToken(): String? {
    return call.request.header("Authorization")
}

fun PipelineContext.requireAuthorizationToken(): String {
    return getAuthorizationToken() ?: throw HttpException(HttpStatusCode.Unauthorized)
}


@Serializable
class GetProfileRequest

@Serializable
data class GetProfileResponse(
    val profile: Profile
)

suspend fun PipelineContext.getProfile() = execute {
    val request = this.call.receive<GetProfileRequest>()
    val token = requireAuthorizationToken()
    val authRepository = Di.get<UserRepository>()
    val user = authRepository.getUserByToken(token).first() ?: throw HttpException(HttpStatusCode.NotFound)
    val response = GetProfileResponse(
        profile = user.profile.requireNotNull()
    )
    call.respond(response)
}


@Serializable
data class UpdateProfileRequest(
    val person: Person,
    val organization: Organization,
    val type: ProfileType
)

@Serializable
data class UpdateProfileResponse(
    val profile: Profile
)

suspend fun PipelineContext.updateProfile() = execute {
    val request = this.call.receive<UpdateProfileRequest>()
    val token = requireAuthorizationToken()
    val userRepository = Di.get<UserRepository>()
    val userByToken = userRepository.getUserByToken(token).first() ?: throw HttpException(HttpStatusCode.Unauthorized)
    val newUser = userByToken.copy(
        profile = userByToken.profile.copy(
            person = request.person,
            organization = request.organization,
            type = request.type
        )
    )
    userRepository.updateUser(newUser.profile.id, newUser).first()
    val response = UpdateProfileResponse(
        profile = newUser.profile
    )
    call.respond(response)
}


@Serializable
class DeleteProfileRequest

@Serializable
class DeleteProfileResponse

suspend fun PipelineContext.deleteProfile() = execute {
    val request = this.call.receive<DeleteProfileRequest>()
    val token = requireAuthorizationToken()
    val userRepository = Di.get<UserRepository>()
    userRepository.deleteUserByToken(token).first()
    val response = DeleteProfileResponse()
    call.respond(response)
}