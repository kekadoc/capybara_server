package com.kekadoc.project.capybara.server.routing.api.profile

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.authToken
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.intercator.profile.ProfileAdminInteractor
import com.kekadoc.project.capybara.server.domain.intercator.profile.ProfileAuthorizedInteractor
import com.kekadoc.project.capybara.server.domain.intercator.profile.ProfileInteractor
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.routing.api.profile.model.*
import com.kekadoc.project.capybara.server.routing.model.RangeDto
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.execution.delete
import com.kekadoc.project.capybara.server.routing.util.execution.get
import com.kekadoc.project.capybara.server.routing.util.execution.patch
import com.kekadoc.project.capybara.server.routing.util.execution.post
import com.kekadoc.project.capybara.server.routing.util.requireParameter
import com.kekadoc.project.capybara.server.routing.util.requirePathId
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.routing.*
import org.koin.core.component.get
import java.util.*

fun Route.profile() = route("/profile") {

    //Создание профиля через токен авторизации
    post<CreateProfileRequestDto> { request -> createProfile(request) }

    //Получение профиля через токен авторизации
    get { getProfileByAuthToken() }

    get("/communications/available") { getAvailableCommunications() }

    //Обновление профиля через токен авторизации
    patch<UpdateProfileRequestDto>("/update/personal") { request -> updateProfileByAuthToken(request) }

    //Обновление пароля через токен авторизации
    patch<UpdateProfilePasswordRequestDto>("/update/password") { request -> updateProfilePasswordByAuthToken(request) }

    //Обновить способы связи авторизованного пользователя
    patch<UpdateUserCommunicationsRequest>("/update/communications") { request -> updateCommunications(request) }

    get("/confirm/email") { confirmEmail() }

    //Получение профиля через токен авторизации
    post<GetProfileListRequestDto>("/list") { request -> getProfileList(request) }

    //
    post<RangeDto>("/list/full") { request -> getFullProfileList(request) }

    route("/{id}") {

        //Получение профиля по идентификатору
        get { getProfileById(requirePathId()) }

        //Получение расширенного профиля по идентификатору
        get("/extended") { getExtendedProfileById(requirePathId()) }

        //Обновление профиля по идентификатору
        patch<UpdateProfileRequestDto>("/update/personal") { request ->
            updateProfileById(
                profileId = requirePathId(),
                request = request,
            )
        }

        //Обновление текущего статуса профиля по идентификатору
        patch<UpdateProfileStatusRequestDto>("/update/status") { request ->
            updateProfileStatusById(
                profileId = requirePathId(),
                request = request,
            )
        }

        //Обновление тип профиля по идентификатору
        patch<UpdateProfileTypeRequestDto>("/update/type") { request ->
            updateProfileTypeById(
                profileId = requirePathId(),
                request = request,
            )
        }

        //Обновление пароля
        patch<UpdateProfilePasswordRequestDto>("/update/password") { request ->
            updatePassword(
                profileId = requirePathId(),
                request = request,
            )
        }

        //Удаление профиля по идентификатору
        delete {
            deleteProfileById(
                profileId = requirePathId(),
            )
        }

        route("/access") {

            route("/user") {

                route("/{targetUserId}") {

                    get {
                        getAccessUser(
                            fromUserId = requirePathId(),
                            toUserId = requireParameter("targetUserId").let(UUID::fromString),
                        )
                    }

                    patch<UpdateAccessUserRequestDto> { request ->
                        updateAccessUser(
                            fromUserId = requirePathId(),
                            toUserId = requireParameter("targetUserId").let(UUID::fromString),
                            request = request,
                        )
                    }

                }

            }

            route("/group") {

                route("/{targetGroupId}") {

                    get {
                        getAccessGroup(
                            userId = requirePathId(),
                            groupId = requireParameter("targetGroupId").let(UUID::fromString)
                        )
                    }

                    patch<UpdateAccessGroupRequestDto> { request ->
                        updateAccessGroup(
                            userId = requirePathId(),
                            groupId = requireParameter("targetGroupId").let(UUID::fromString),
                            request = request,
                        )
                    }

                }

            }

        }

        route("/communications") {

            //Обновить способы связи пользователя
            patch<UpdateUserCommunicationsRequest> { request ->
                updateCommunications(
                    profileId = requirePathId(),
                    request = request,
                )
            }

        }

    }

}


private suspend fun PipelineContext.confirmEmail() = execute() {
    Di.get<ProfileInteractor>().confirmEmail(
        confirmationToken = requireParameter("token")
    )
}

//By AccessToken

private suspend fun PipelineContext.createProfile(
    request: CreateProfileRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAdminInteractor>().createProfile(
        adminAccessToken = authToken,
        request = request,
    )
}

private suspend fun PipelineContext.getProfileByAuthToken(

) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAuthorizedInteractor>().getProfile(
        accessToken = authToken,
    )
}

private suspend fun PipelineContext.updateProfileByAuthToken(
    request: UpdateProfileRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAuthorizedInteractor>().updateProfile(
        accessToken = authToken,
        request = request,
    )
}

private suspend fun PipelineContext.updateProfilePasswordByAuthToken(
    request: UpdateProfilePasswordRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAuthorizedInteractor>().updateProfilePassword(
        accessToken = authToken,
        request = request,
    )
}

private suspend fun PipelineContext.updateCommunications(
    request: UpdateUserCommunicationsRequest,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAuthorizedInteractor>().updateCommunications(
        accessToken = authToken,
        request = request,
    )
}

private suspend fun PipelineContext.getProfileList(
    request: GetProfileListRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAuthorizedInteractor>().getProfiles(
        accessToken = authToken,
        profileIds = request.ids.map(UUID::fromString),
    )
}

private suspend fun PipelineContext.getFullProfileList(
    request: RangeDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAuthorizedInteractor>().getExtendedProfilesWithRange(
        accessToken = authToken,
        range = request,
    )
}

private suspend fun PipelineContext.getAvailableCommunications(

) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAuthorizedInteractor>().getAvailableCommunications(
        accessToken = authToken,
    )
}


//By Identifiers

private suspend fun PipelineContext.getProfileById(
    profileId: Identifier,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAuthorizedInteractor>().getProfileById(
        accessToken = authToken,
        profileId = profileId,
    )
}

private suspend fun PipelineContext.getExtendedProfileById(
    profileId: Identifier,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAdminInteractor>().getExtendedProfile(
        adminAccessToken = authToken,
        profileId = profileId,
    )
}

private suspend fun PipelineContext.updateProfileById(
    profileId: Identifier,
    request: UpdateProfileRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAdminInteractor>().updateProfilePersonal(
        adminAccessToken = authToken,
        profileId = profileId,
        request = request,
    )
}

private suspend fun PipelineContext.updateProfileStatusById(
    profileId: Identifier,
    request: UpdateProfileStatusRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAdminInteractor>().updateProfileStatus(
        adminAccessToken = authToken,
        profileId = profileId,
        request = request,
    )
}

private suspend fun PipelineContext.updateProfileTypeById(
    profileId: Identifier,
    request: UpdateProfileTypeRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAdminInteractor>().updateProfileType(
        adminAccessToken = authToken,
        profileId = profileId,
        request = request,
    )
}

private suspend fun PipelineContext.updatePassword(
    profileId: Identifier,
    request: UpdateProfilePasswordRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAdminInteractor>().updateProfilePassword(
        adminAccessToken = authToken,
        profileId = profileId,
        request = request,
    )
}

private suspend fun PipelineContext.deleteProfileById(
    profileId: Identifier,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAdminInteractor>().deleteProfile(
        adminAccessToken = authToken,
        profileId = profileId,
    )
}

private suspend fun PipelineContext.getAccessUser(
    fromUserId: Identifier,
    toUserId: Identifier,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAdminInteractor>().getAccessUser(
        adminAccessToken = authToken,
        fromUserId = fromUserId,
        toUserId = toUserId,
    )
}

private suspend fun PipelineContext.updateAccessUser(
    fromUserId: Identifier,
    toUserId: Identifier,
    request: UpdateAccessUserRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAdminInteractor>().updateAccessUser(
        adminAccessToken = authToken,
        fromUserId = fromUserId,
        toUserId = toUserId,
        request = request,
    )
}

private suspend fun PipelineContext.getAccessGroup(
    userId: Identifier,
    groupId: Identifier,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAdminInteractor>().getAccessGroup(
        adminAccessToken = authToken,
        userId = userId,
        groupId = groupId,
    )
}

private suspend fun PipelineContext.updateAccessGroup(
    userId: Identifier,
    groupId: Identifier,
    request: UpdateAccessGroupRequestDto,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAdminInteractor>().updateAccessGroup(
        adminAccessToken = authToken,
        userId = userId,
        groupId = groupId,
        request = request,
    )
}

private suspend fun PipelineContext.updateCommunications(
    profileId: Identifier,
    request: UpdateUserCommunicationsRequest,
) = execute(ApiKeyVerifier, AuthorizationVerifier) {
    Di.get<ProfileAdminInteractor>().updateProfileCommunications(
        adminAccessToken = authToken,
        profileId = profileId,
        request = request,
    )
}