package com.kekadoc.project.capybara.server.routing.api.profile

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.extensions.requireNotNull
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.intercator.profile.ProfileInteractor
import com.kekadoc.project.capybara.server.routing.api.profile.model.*
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.execution.delete
import com.kekadoc.project.capybara.server.routing.util.execution.get
import com.kekadoc.project.capybara.server.routing.util.execution.patch
import com.kekadoc.project.capybara.server.routing.util.execution.post
import com.kekadoc.project.capybara.server.routing.util.requirePathId
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.get
import java.util.*

fun Route.profile() = route("/profile") {

    //Создание профиля через токен авторизации
    post<CreateProfileRequest> { request -> createProfile(request) }

    //Получение профиля через токен авторизации
    get { getProfileByAuthToken() }

    //Обновление профиля через токен авторизации
    patch<UpdateProfileRequest> { request -> updateProfileByAuthToken(request) }

    //Обновить способы связи авторизованного пользователя
    post<UpdateUserCommunicationsRequest>("/communications") { request -> updateCommunications(request) }

    route("/{id}") {

        //Получение профиля по идентификатору
        get { execute { getProfileById(requirePathId()) } }

        //Обновление профиля по идентификатору
        patch<UpdateProfileRequest> { request ->
            execute {
                updateProfileById(
                    profileId = requirePathId(),
                    request = request,
                )
            }
        }

        //Обновление тип профиля по идентификатору
        patch<UpdateProfileTypeRequest>("/type") { request ->
            execute {
                updateProfileTypeById(
                    profileId = requirePathId(),
                    request = request,
                )
            }
        }

        //Удаление профиля по идентификатору
        delete { execute { deleteProfileById(requirePathId()) } }

        route("/access") {

            route("/user") {

                route("/{targetUserId}") {

                    get {
                        execute {
                            getAccessUser(
                                fromUserId = requirePathId(),
                                toUserId = call.parameters["targetUserId"].requireNotNull().let(UUID::fromString),
                            )
                        }
                    }

                    patch<UpdateAccessUserRequestDto> { request ->
                        execute {
                            updateAccessUser(
                                fromUserId = requirePathId(),
                                toUserId = call.parameters["targetUserId"].requireNotNull().let(UUID::fromString),
                                request = request,
                            )
                        }
                    }

                }

            }

            route("/group") {

                route("/{targetGroupId}") {

                    get {
                        execute {
                            getAccessGroup(
                                userId = requirePathId(),
                                groupId = call.parameters["targetGroupId"].requireNotNull().let(UUID::fromString)
                            )
                        }
                    }

                    patch<UpdateAccessGroupRequestDto> { request ->
                        execute {
                            updateAccessGroup(
                                userId = requirePathId(),
                                groupId = call.parameters["targetGroupId"].requireNotNull().let(UUID::fromString),
                                request = request,
                            )
                        }
                    }

                }

            }

        }

        route("/communications") {

            //Обновить способы связи пользователя
            post<UpdateUserCommunicationsRequest> { request ->
                execute {
                    updateCommunications(
                        profileId = requirePathId(),
                        request = request,
                    )
                }
            }

        }

    }

}

private suspend fun PipelineContext.createProfile(
    request: CreateProfileRequest,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.createProfile(
        authToken = authToken,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getProfileByAuthToken(

) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.getProfileByAuthToken(
        authToken = authToken,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateProfileByAuthToken(
    request: UpdateProfileRequest,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateProfileByAuthToken(
        authToken = authToken,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateCommunications(
    request: UpdateUserCommunicationsRequest,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateCommunications(
        authToken = authToken,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateCommunications(
    profileId: Identifier,
    request: UpdateUserCommunicationsRequest,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateCommunications(
        authToken = authToken,
        profileId = profileId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getProfileById(
    profileId: Identifier,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.getProfileById(
        authToken = authToken,
        profileId = profileId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateProfileById(
    profileId: Identifier,
    request: UpdateProfileRequest,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateProfileById(
        authToken = authToken,
        profileId = profileId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateProfileTypeById(
    profileId: Identifier,
    request: UpdateProfileTypeRequest,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateProfileTypeById(
        authToken = authToken,
        profileId = profileId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.deleteProfileById(
    profileId: Identifier,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.deleteProfileById(
        authToken = authToken,
        profileId = profileId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getAccessUser(
    fromUserId: Identifier,
    toUserId: Identifier,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.getAccessUser(
        authToken = authToken,
        fromUserId = fromUserId,
        toUserId = toUserId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateAccessUser(
    fromUserId: Identifier,
    toUserId: Identifier,
    request: UpdateAccessUserRequestDto,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateAccessUser(
        authToken = authToken,
        fromUserId = fromUserId,
        toUserId = toUserId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getAccessGroup(
    userId: Identifier,
    groupId: Identifier,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.getAccessGroup(
        authToken = authToken,
        userId = userId,
        groupId = groupId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateAccessGroup(
    userId: Identifier,
    groupId: Identifier,
    request: UpdateAccessGroupRequestDto,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateAccessGroup(
        authToken = authToken,
        userId = userId,
        groupId = groupId,
        request = request,
    )
    call.respond(result)
}