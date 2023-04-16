package com.kekadoc.project.capybara.server.intercator.profile

import com.kekadoc.project.capybara.server.routing.api.profile.model.*

interface ProfileInteractor {

    suspend fun createAdmin(
        request: CreateAdminRequest,
    ): CreateProfileResponse

    suspend fun createProfile(
        authToken: String,
        request: CreateProfileRequest,
    ): CreateProfileResponse


    suspend fun getProfileByAuthToken(
        authToken: String,
    ): GetProfileResponse

    suspend fun updateProfileByAuthToken(
        authToken: String,
        request: UpdateProfileRequest,
    ): UpdateProfileResponse


    suspend fun updatePushTokenByAuthToken(
        authToken: String,
        request: UpdatePushTokenRequest,
    )

    suspend fun deletePushTokenByAuthToken(
        authToken: String,
    )


    suspend fun getProfileById(
        authToken: String,
        profileId: String,
    ): GetProfileResponse

    suspend fun updateProfileById(
        authToken: String,
        profileId: String,
        request: UpdateProfileRequest,
    ): UpdateProfileResponse

    suspend fun updateProfileTypeById(
        authToken: String,
        profileId: String,
        request: UpdateProfileTypeRequest,
    ): UpdateProfileTypeResponse

    suspend fun deleteProfileById(
        authToken: String,
        profileId: String,
    )

    suspend fun deletePushTokenById(
        authToken: String,
        profileId: String,
    )

    suspend fun updateUserGroupAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserGroupsRequest,
    )
    suspend fun updateUserGroupRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserGroupsRequest,
    )

    suspend fun updateUserCommunicationsAddresseesGroupAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    )
    suspend fun updateUserCommunicationsAddresseesGroupRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    )

    suspend fun updateUserCommunicationsAddresseesUserAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    )
    suspend fun updateUserCommunicationsAddresseesUserRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    )

    suspend fun updateUserCommunicationsContactsAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    )
    suspend fun updateUserCommunicationsContactsRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    )

}