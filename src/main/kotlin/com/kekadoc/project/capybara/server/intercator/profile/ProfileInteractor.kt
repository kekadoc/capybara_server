package com.kekadoc.project.capybara.server.intercator.profile

import com.kekadoc.project.capybara.server.routing.api.profile.model.*

interface ProfileInteractor {

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

    suspend fun updateUserAvailabilityGroupAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    )
    suspend fun updateUserAvailabilityGroupRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    )

    suspend fun updateUserAvailabilityUserAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    )
    suspend fun updateUserAvailabilityRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    )

    suspend fun updateUserAvailabilityContactsAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    )
    suspend fun updateUserAvailabilityContactsRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    )

}