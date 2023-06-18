package com.kekadoc.project.capybara.server.domain.intercator.profile

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.profile.model.*
import com.kekadoc.project.capybara.server.routing.model.RangeDto

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

    suspend fun getExtendedProfilesWithRange(
        accessToken: Token,
        range: RangeDto,
    ): GetFullProfileListResponseDto

    suspend fun updateProfile(
        accessToken: Token,
        request: UpdateProfileRequestDto,
    ): UpdateProfileResponseDto

    suspend fun updateProfilePassword(
        accessToken: Token,
        request: UpdateProfilePasswordRequestDto,
    ): UpdateProfilePasswordResponseDto

    suspend fun getAvailableCommunications(
        accessToken: Token,
    ): GetAvailableCommunicationsDto

    suspend fun updateCommunications(
        accessToken: Token,
        request: UpdateUserCommunicationsRequestDto,
    ): UpdateUserCommunicationsResponseDto

}