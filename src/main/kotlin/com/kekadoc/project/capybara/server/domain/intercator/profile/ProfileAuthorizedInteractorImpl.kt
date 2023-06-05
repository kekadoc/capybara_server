@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kekadoc.project.capybara.server.domain.intercator.profile

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.domain.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.domain.model.*
import com.kekadoc.project.capybara.server.routing.api.profile.model.*
import com.kekadoc.project.capybara.server.routing.model.RangeDto
import com.kekadoc.project.capybara.server.routing.model.converter.RangeDtoConverter
import com.kekadoc.project.capybara.server.routing.model.factory.ExtendedProfileDtoFactory
import com.kekadoc.project.capybara.server.routing.model.factory.ProfileDtoFactory
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class ProfileAuthorizedInteractorImpl(
    private val userRepository: UsersRepository,
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
) : ProfileAuthorizedInteractor {

    override suspend fun getProfile(
        accessToken: Token,
    ): GetExtendedProfileResponseDto = fetchUserByAccessTokenFunction.fetchUser(accessToken)
        .map(ExtendedProfileDtoFactory::create)
        .map(::GetExtendedProfileResponseDto)
        .single()

    override suspend fun getProfileById(
        accessToken: Token,
        profileId: Identifier,
    ): GetProfileResponse = fetchUserByAccessTokenFunction.fetchUser(accessToken)
        .flatMapLatest { user ->
            if (user.id == profileId) {
                flowOf(true)
            } else {
                userRepository.getAccessForUser(
                    userId = profileId,
                    forUserId = user.id,
                )
                    .map { access -> !access.readProfile }
            }
        }
        .flatMapLatest { readProfile ->
            if (readProfile) userRepository.getUserById(profileId)
            else throw HttpException(HttpStatusCode.Forbidden)
        }
        .map(ProfileDtoFactory::create)
        .map(::GetProfileResponse)
        .single()

    override suspend fun getProfiles(
        accessToken: Token,
        profileIds: List<Identifier>,
    ): GetProfileListResponseDto = fetchUserByAccessTokenFunction.fetchUser(accessToken)
        .flatMapLatest { user ->
            if (user.isAdmin()) flowOf(true)
            else userRepository.getAccessForUsers(user.id, profileIds)
                .map { accessList -> accessList.all { access -> access.readProfile } }
        }
        .flatMapLatest { readProfile ->
            if (readProfile) userRepository.getUsersByIds(profileIds)
            else throw HttpException(HttpStatusCode.Forbidden)
        }
        .mapElements(ProfileDtoFactory::create)
        .map(::GetProfileListResponseDto)
        .single()

    override suspend fun getExtendedProfilesWithRange(
        accessToken: Token,
        range: RangeDto
    ): GetFullProfileListResponseDto = fetchUserByAccessTokenFunction.fetchUser(accessToken)
        .requireAdminUser()
        .flatMapLatest { userRepository.getUsers(RangeDtoConverter.convert(range)) }
        .mapElements(ExtendedProfileDtoFactory::create)
        .map(::GetFullProfileListResponseDto)
        .single()

    override suspend fun updateProfile(
        accessToken: Token,
        request: UpdateProfileRequestDto,
    ): UpdateProfileResponseDto = fetchUserByAccessTokenFunction.fetchUser(accessToken)
        .flatMapLatest { user ->
            userRepository.updateUserProfile(
                userId = user.id,
                profile = user.profile.copy(
                    name = request.name,
                    surname = request.surname,
                    patronymic = request.patronymic,
                    about = request.about,
                ),
            )
        }
        .map(ExtendedProfileDtoFactory::create)
        .map(::UpdateProfileResponseDto)
        .single()

    override suspend fun updateProfilePassword(
        accessToken: Token,
        request: UpdateProfilePasswordRequestDto,
    ): UpdateProfilePasswordResponseDto = fetchUserByAccessTokenFunction.fetchUser(accessToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            userRepository.updateUserPassword(
                userId = user.id,
                newPassword = request.password,
            )
        }
        .map(ExtendedProfileDtoFactory::create)
        .map(::UpdateProfilePasswordResponseDto)
        .single()

    override suspend fun updateCommunications(
        accessToken: Token,
        request: UpdateUserCommunicationsRequest,
    ): UpdateUserCommunicationsResponseDto = fetchUserByAccessTokenFunction.fetchUser(accessToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            userRepository.updateUserCommunications(
                userId = user.id,
                communications = Communications(
                    values = request.values.map { (type, value) ->
                        Communication(
                            type = type,
                            value = value,
                            approved = false,
                        )
                    }
                ),
            )
        }
        .map(ExtendedProfileDtoFactory::create)
        .map(::UpdateUserCommunicationsResponseDto)
        .single()

}