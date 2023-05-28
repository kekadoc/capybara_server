package com.kekadoc.project.capybara.server.domain.intercator.profile

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.profile.model.*

interface ProfileAuthorizedInteractor {

    suspend fun getProfile(
        accessToken: Token,
    ): GetExtendedProfileResponseDto

    suspend fun getProfileById(
        accessToken: Token,
        profileId: Identifier,
    ): GetProfileResponse

    suspend fun getProfiles(
        accessToken: Token,
        profileIds: List<Identifier>,
    ): GetProfileListResponseDto

    suspend fun updateProfile(
        accessToken: Token,
        request: UpdateProfileRequestDto,
    ): UpdateProfileResponseDto

    suspend fun updateProfilePassword(
        accessToken: Token,
        request: UpdateProfilePasswordRequestDto,
    ): UpdateProfilePasswordResponseDto

    suspend fun updateCommunications(
        accessToken: Token,
        request: UpdateUserCommunicationsRequest,
    ): UpdateUserCommunicationsResponseDto

}