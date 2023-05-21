@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kekadoc.project.capybara.server.domain.intercator.profile

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.extensions.swap
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.network.model.factory.ExtendedProfileDtoFactory
import com.kekadoc.project.capybara.server.data.source.network.model.factory.ProfileDtoFactory
import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.domain.model.Communication
import com.kekadoc.project.capybara.server.domain.model.Communications
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.profile.model.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

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
        .swap(::UpdateUserCommunicationsResponseDto)
        .single()

}