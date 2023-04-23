@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package com.kekadoc.project.capybara.server.intercator.profile

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.repository.notification.mobile.MobileNotificationsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.converter.dto.ProfileDtoConverter
import com.kekadoc.project.capybara.server.data.source.network.model.ProfileDto
import com.kekadoc.project.capybara.server.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.intercator.requireUser
import com.kekadoc.project.capybara.server.routing.api.profile.model.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileInteractorImpl(
    private val userRepository: UsersRepository,
    private val mobileNotificationsRepository: MobileNotificationsRepository,
) : ProfileInteractor {

    override suspend fun createProfile(
        authToken: String,
        request: CreateProfileRequest,
    ): CreateProfileResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            userRepository.getUserByLogin(request.login).flatMapLatest { userByLogin ->
                if (userByLogin != null) {
                    throw HttpException(
                        statusCode = HttpStatusCode.BadRequest,
                        message = "User by this login already exist",
                    )
                } else {
                    userRepository.createUser(
                        login = request.login,
                        profile = Profile(
                            type = ProfileDtoConverter.TypeConverter.revert(request.type),
                            name = request.name,
                            surname = request.surname,
                            patronymic = request.patronymic,
                            avatar = request.avatar,
                            role = request.role,
                            about = request.about,
                        ),
                    )
                }
            }
        }
        .map { newUser ->
            CreateProfileResponse(
                profile = newUser.profile.let { ProfileDtoConverter.revert(it) },
                password = newUser.password
            )
        }
        .single()

    override suspend fun getProfileByAuthToken(
        authToken: String,
    ): GetProfileResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .map { user -> user.profile.let { ProfileDtoConverter.revert(it) } }
        .map(::GetProfileResponse)
        .single()

    override suspend fun updateProfileByAuthToken(
        authToken: String,
        request: UpdateProfileRequest,
    ): UpdateProfileResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            userRepository.updateUserProfile(
                userId = user.id,
                profile = user.profile.copy(
                    name = request.name,
                    surname = request.surname,
                    patronymic = request.patronymic,
                    avatar = request.avatar,
                    role = request.role,
                    about = request.about,
                ),
            )
        }
        .map { newUser -> UpdateProfileResponse(newUser.profile.let { ProfileDtoConverter.revert(it) }) }
        .single()


    override suspend fun getProfileById(
        authToken: String,
        profileId: String,
    ): GetProfileResponse {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .map { user ->
                        user?.profile ?: throw HttpException(HttpStatusCode.NotFound)
                    }
                    .map { ProfileDtoConverter.revert(it) }
        }.map(::GetProfileResponse).single()
    }

    override suspend fun updateProfileById(
        authToken: String,
        profileId: String,
        request: UpdateProfileRequest,
    ): UpdateProfileResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { user ->
            userRepository.updateUserProfile(
                userId = profileId,
                profile = user.profile.copy(
                    name = request.name,
                    surname = request.surname,
                    patronymic = request.patronymic,
                    avatar = request.avatar,
                    role = request.role,
                    about = request.about,
                ),
            )
        }
        .map { newUser -> newUser.profile.let { ProfileDtoConverter.revert(it) } }
        .map(::UpdateProfileResponse)
        .single()

    override suspend fun updateProfileTypeById(
        authToken: String,
        profileId: String,
        request: UpdateProfileTypeRequest,
    ): UpdateProfileTypeResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { user ->
            userRepository.updateUserProfile(
                userId = profileId,
                profile = user.profile.copy(
                    type = when (request.type) {
                        ProfileDto.Type.USER -> Profile.Type.USER
                        ProfileDto.Type.SPEAKER -> Profile.Type.SPEAKER
                        ProfileDto.Type.ADMIN -> Profile.Type.ADMIN
                        ProfileDto.Type.DEFAULT -> Profile.Type.DEFAULT
                    }
                )
            )
        }
        .map { newUser -> newUser.profile.let { ProfileDtoConverter.revert(it) } }
        .map(::UpdateProfileTypeResponse)
        .single()

    override suspend fun deleteProfileById(
        authToken: String,
        profileId: String,
    ) = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { userRepository.deleteUser(profileId) }
        .single()

    override suspend fun updateUserAvailabilityGroupAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .requireUser()
                    .flatMapLatest {
                        userRepository.updateUserAvailabilityGroupsAdd(
                            userId = profileId,
                            groupIds = request.ids.toSet(),
                        )
                    }

            }
            .single()
    }

    override suspend fun updateUserAvailabilityGroupRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .requireUser()
                    .flatMapLatest {
                        userRepository.updateUserAvailabilityGroupsRemove(
                            userId = profileId,
                            groupIds = request.ids.toSet(),
                        )
                    }

            }
            .single()
    }

    override suspend fun updateUserAvailabilityUserAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .requireUser()
                    .flatMapLatest {
                        userRepository.updateUserAvailabilityUsersAdd(
                            userId = profileId,
                            userIds = request.ids.toSet(),
                        )
                    }

            }
            .single()
    }

    override suspend fun updateUserAvailabilityRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .requireUser()
                    .flatMapLatest {
                        userRepository.updateUserAvailabilityUsersRemove(
                            userId = profileId,
                            userIds = request.ids.toSet(),
                        )
                    }

            }
            .single()
    }

    override suspend fun updateUserAvailabilityContactsAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .requireUser()
                    .flatMapLatest {
                        userRepository.updateUserAvailabilityContactsAdd(
                            userId = profileId,
                            contactIds = request.ids.toSet(),
                        )
                    }

            }
            .single()
    }

    override suspend fun updateUserAvailabilityContactsRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .requireUser()
                    .flatMapLatest {
                        userRepository.updateUserAvailabilityContactsRemove(
                            userId = profileId,
                            contactIds = request.ids.toSet(),
                        )
                    }

            }
            .single()
    }


}