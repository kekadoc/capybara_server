package com.kekadoc.project.capybara.server.domain.intercator.profile

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.profile.model.*

interface ProfileInteractor {

    suspend fun createProfile(
        authToken: Token,
        request: CreateProfileRequest,
    ): CreateProfileResponse

    suspend fun getProfileByAuthToken(
        authToken: Token,
    ): GetAuthorizedProfileResponse

    suspend fun updateProfileByAuthToken(
        authToken: Token,
        request: UpdateProfileRequest,
    ): UpdateProfileResponse

    suspend fun getProfileById(
        authToken: Token,
        profileId: Identifier,
    ): GetProfileResponse

    suspend fun updateProfileById(
        authToken: Token,
        profileId: Identifier,
        request: UpdateProfileRequest,
    ): UpdateProfileResponse

    suspend fun updateProfileTypeById(
        authToken: Token,
        profileId: Identifier,
        request: UpdateProfileTypeRequest,
    ): UpdateProfileTypeResponse

    suspend fun deleteProfileById(
        authToken: Token,
        profileId: Identifier,
    )

    suspend fun getAccessUser(
        authToken: Token,
        fromUserId: Identifier,
        toUserId: Identifier,
    ): GetAccessUserResponseDto

    suspend fun updateAccessUser(
        authToken: Token,
        fromUserId: Identifier,
        toUserId: Identifier,
        request: UpdateAccessUserRequestDto,
    ): UpdateAccessUserResponseDto

    suspend fun getAccessGroup(
        authToken: Token,
        userId: Identifier,
        groupId: Identifier,
    ): GetAccessGroupResponseDto

    suspend fun updateAccessGroup(
        authToken: Token,
        userId: Identifier,
        groupId: Identifier,
        request: UpdateAccessGroupRequestDto,
    ): UpdateAccessGroupResponseDto

    suspend fun updateCommunications(
        authToken: Token,
        request: UpdateUserCommunicationsRequest,
    ): UpdateUserCommunicationsResponseDto

    suspend fun updateCommunications(
        authToken: Token,
        profileId: Identifier,
        request: UpdateUserCommunicationsRequest,
    ): UpdateUserCommunicationsResponseDto

}