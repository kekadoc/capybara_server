package com.kekadoc.project.capybara.server.domain.intercator.profile

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.profile.model.*

interface ProfileAdminInteractor {

    suspend fun createProfile(
        adminAccessToken: Token,
        request: CreateProfileRequestDto,
    ): CreateProfileResponseDto

    suspend fun getExtendedProfile(
        adminAccessToken: Token,
        profileId: Identifier,
    ): GetExtendedProfileResponseDto

    suspend fun updateProfilePersonal(
        adminAccessToken: Token,
        profileId: Identifier,
        request: UpdateProfileRequestDto,
    ): UpdateProfileResponseDto

    suspend fun updateProfileStatus(
        adminAccessToken: Token,
        profileId: Identifier,
        request: UpdateProfileStatusRequestDto,
    ): UpdateProfileStatusResponseDto

    suspend fun updateProfileType(
        adminAccessToken: Token,
        profileId: Identifier,
        request: UpdateProfileTypeRequestDto,
    ): UpdateProfileTypeResponseDto

    suspend fun updateProfilePassword(
        adminAccessToken: Token,
        profileId: Identifier,
        request: UpdateProfilePasswordRequestDto,
    ): UpdateProfilePasswordResponseDto

    suspend fun deleteProfile(
        adminAccessToken: Token,
        profileId: Identifier,
    ): DeleteProfileResponseDto

    suspend fun getAccessUser(
        adminAccessToken: Token,
        fromUserId: Identifier,
        toUserId: Identifier,
    ): GetAccessUserResponseDto

    suspend fun updateAccessUser(
        adminAccessToken: Token,
        fromUserId: Identifier,
        toUserId: Identifier,
        request: UpdateAccessUserRequestDto,
    ): UpdateAccessUserResponseDto

    suspend fun getAccessGroup(
        adminAccessToken: Token,
        userId: Identifier,
        groupId: Identifier,
    ): GetAccessGroupResponseDto

    suspend fun updateAccessGroup(
        adminAccessToken: Token,
        userId: Identifier,
        groupId: Identifier,
        request: UpdateAccessGroupRequestDto,
    ): UpdateAccessGroupResponseDto

    suspend fun updateProfileCommunications(
        adminAccessToken: Token,
        profileId: Identifier,
        request: UpdateUserCommunicationsRequest,
    ): UpdateUserCommunicationsResponseDto

}