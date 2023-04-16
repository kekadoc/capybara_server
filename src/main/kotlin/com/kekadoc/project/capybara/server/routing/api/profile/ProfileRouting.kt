package com.kekadoc.project.capybara.server.routing.api.profile

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.intercator.profile.ProfileInteractor
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

fun Route.profile() = route("/profile") {

    //Создание профиля через токен авторизации
    post<CreateAdminRequest>("/admin") { request -> createAdmin(request) }

    //Создание профиля через токен авторизации
    post<CreateProfileRequest> { request -> createProfile(request) }

    //Получение профиля через токен авторизации
    get { getProfileByAuthToken() }

    //Обновление профиля через токен авторизации
    patch<UpdateProfileRequest> { request -> updateProfileByAuthToken(request) }

    //Работа с пушами
    route("/push") {

        //Обновление пуш токена через токен авторизации
        post<UpdatePushTokenRequest> { request -> updatePushTokenByAuthToken(request) }

        //Удаление пуш токена через токен авторизации
        delete { deletePushTokenByAuthToken() }

    }

    route("/{id}") {

        //Получение профиля по идентификатору
        get { getProfileById(requirePathId()) }

        //Обновление профиля по идентификатору
        patch<UpdateProfileRequest> { request -> updateProfileById(requirePathId(), request) }

        //Обновление тип профиля по идентификатору
        patch<UpdateProfileTypeRequest>("type") { request -> updateProfileTypeById(requirePathId(), request) }

        route("/communications") {

            route("/addressees") {

                route("/group") {
                    patch<UpdateUserCommunicationsRequest>(
                        path = "/add",
                        ApiKeyVerifier, AuthorizationVerifier,
                    ) { request ->
                        updateUserCommunicationsAddresseesGroupAdd(
                            profileId = requirePathId(),
                            request = request,
                        )
                    }
                    patch<UpdateUserCommunicationsRequest>(
                        path = "/remove",
                        ApiKeyVerifier, AuthorizationVerifier,
                    ) { request ->
                        updateUserCommunicationsAddresseesGroupRemove(
                            profileId = requirePathId(),
                            request = request,
                        )
                    }
                }

                route("/users") {
                    patch<UpdateUserCommunicationsRequest>(
                        path = "/add",
                        ApiKeyVerifier, AuthorizationVerifier,
                    ) { request ->
                        updateUserCommunicationsAddresseesUserAdd(
                            profileId = requirePathId(),
                            request = request,
                        )
                    }
                    patch<UpdateUserCommunicationsRequest>(
                        path = "/remove",
                        ApiKeyVerifier, AuthorizationVerifier,
                    ) { request ->
                        updateUserCommunicationsAddresseesUserRemove(
                            profileId = requirePathId(),
                            request = request,
                        )
                    }
                }

            }
            route("/contacts") {
                patch<UpdateUserCommunicationsRequest>(
                    path = "/add",
                    ApiKeyVerifier, AuthorizationVerifier,
                ) { request ->
                    updateUserCommunicationsContactsAdd(
                        profileId = requirePathId(),
                        request = request,
                    )
                }
                patch<UpdateUserCommunicationsRequest>(
                    path = "/remove",
                    ApiKeyVerifier, AuthorizationVerifier,
                ) { request ->
                    updateUserCommunicationsContactsRemove(
                        profileId = requirePathId(),
                        request = request,
                    )
                }
            }

        }

        route("/groups") {

            patch<UpdateUserGroupsRequest>(
                "/add",
                ApiKeyVerifier, AuthorizationVerifier,
            ) { request ->
                updateUserGroupAdd(
                    profileId = requirePathId(),
                    request = request,
                )
            }

            patch<UpdateUserGroupsRequest>(
                "/remove",
                ApiKeyVerifier, AuthorizationVerifier,
            ) { request ->
                updateUserGroupRemove(
                    profileId = requirePathId(),
                    request = request,
                )
            }

        }

        //Удаление профиля по идентификатору
        delete { deleteProfileById(requirePathId()) }

        //Пуши
        route("/push") {

            //Удаление пуштокена по идентификатору
            delete { deletePushTokenById(requirePathId()) }

        }

    }

}

private suspend fun PipelineContext.createAdmin(
    request: CreateAdminRequest,
) = execute {
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.createAdmin(
        request = request,
    )
    call.respond(result)
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

private suspend fun PipelineContext.updatePushTokenByAuthToken(
    request: UpdatePushTokenRequest,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updatePushTokenByAuthToken(
        authToken = authToken,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.deletePushTokenByAuthToken(

) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.deletePushTokenByAuthToken(
        authToken = authToken,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getProfileById(
    profileId: String,
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
    profileId: String,
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
    profileId: String,
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
    profileId: String,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.deleteProfileById(
        authToken = authToken,
        profileId = profileId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.deletePushTokenById(
    profileId: String,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.deletePushTokenById(
        authToken = authToken,
        profileId = profileId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateUserGroupAdd(
    profileId: String,
    request: UpdateUserGroupsRequest,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateUserGroupAdd(
        authToken = authToken,
        profileId = profileId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateUserGroupRemove(
    profileId: String,
    request: UpdateUserGroupsRequest,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateUserGroupRemove(
        authToken = authToken,
        profileId = profileId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateUserCommunicationsAddresseesGroupAdd(
    profileId: String,
    request: UpdateUserCommunicationsRequest,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateUserCommunicationsAddresseesGroupAdd(
        authToken = authToken,
        profileId = profileId,
        request = request,
    )
    call.respond(result)
}
private suspend fun PipelineContext.updateUserCommunicationsAddresseesGroupRemove(
    profileId: String,
    request: UpdateUserCommunicationsRequest,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateUserCommunicationsAddresseesGroupRemove(
        authToken = authToken,
        profileId = profileId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateUserCommunicationsAddresseesUserAdd(
    profileId: String,
    request: UpdateUserCommunicationsRequest,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateUserCommunicationsAddresseesUserAdd(
        authToken = authToken,
        profileId = profileId,
        request = request,
    )
    call.respond(result)
}
private suspend fun PipelineContext.updateUserCommunicationsAddresseesUserRemove(
    profileId: String,
    request: UpdateUserCommunicationsRequest,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateUserCommunicationsAddresseesUserRemove(
        authToken = authToken,
        profileId = profileId,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.updateUserCommunicationsContactsAdd(
    profileId: String,
    request: UpdateUserCommunicationsRequest,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateUserCommunicationsContactsAdd(
        authToken = authToken,
        profileId = profileId,
        request = request,
    )
    call.respond(result)
}
private suspend fun PipelineContext.updateUserCommunicationsContactsRemove(
    profileId: String,
    request: UpdateUserCommunicationsRequest,
) {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ProfileInteractor>()
    val result = interactor.updateUserCommunicationsContactsRemove(
        authToken = authToken,
        profileId = profileId,
        request = request,
    )
    call.respond(result)
}