@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package com.kekadoc.project.capybara.server.intercator.profile

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.extensions.emptyString
import com.kekadoc.project.capybara.server.data.model.user.ProfileType
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.profile.model.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileInteractorImpl(
    private val userRepository: UsersRepository,
) : ProfileInteractor {

    override suspend fun createAdmin(
        request: CreateAdminRequest,
    ): CreateProfileResponse {
        return userRepository.getUserByLogin(request.login)
            .flatMapLatest { userByLogin ->
                if (userByLogin != null) {
                    throw HttpException(
                        statusCode = HttpStatusCode.BadRequest,
                        message = "User by this login already exist",
                    )
                } else {
                    userRepository.createUser(
                        login = request.login,
                        person = request.person,
                        type = ProfileType.ADMIN,
                    )
                }
            }
            .map { newUser ->
                CreateProfileResponse(
                    profile = newUser.profile,
                    password = newUser.password
                )
            }
            .single()
    }

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
                        person = request.person,
                        type = request.type,
                    )
                }
            }
        }
        .map { newUser ->
            CreateProfileResponse(
                profile = newUser.profile,
                password = newUser.password
            )
        }
        .single()

    override suspend fun getProfileByAuthToken(
        authToken: String,
    ): GetProfileResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .map { user -> user.profile }
        .map(::GetProfileResponse)
        .single()

    override suspend fun updateProfileByAuthToken(
        authToken: String,
        request: UpdateProfileRequest,
    ): UpdateProfileResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            userRepository.updateUserPersonal(
                id = user.id,
                person = request.person,
            )
        }
        .map { newUser -> UpdateProfileResponse(newUser.profile) }
        .single()

    override suspend fun updatePushTokenByAuthToken(
        authToken: String,
        request: UpdatePushTokenRequest,
    ) = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            userRepository.updateUserPushToken(
                id = user.id,
                pushToken = request.token,
            )
        }
        .map { }
        .single()

    override suspend fun deletePushTokenByAuthToken(
        authToken: String,
    ) = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            userRepository.updateUserPushToken(
                id = user.id,
                pushToken = emptyString(),
            )
        }
        .map { }
        .single()


    override suspend fun getProfileById(
        authToken: String,
        profileId: String,
    ): GetProfileResponse {
        println("getProfileById $authToken $profileId")
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .map { user ->
                        user?.profile ?: throw HttpException(HttpStatusCode.NotFound)
                    }
        }.map(::GetProfileResponse).single()
    }

    override suspend fun updateProfileById(
        authToken: String,
        profileId: String,
        request: UpdateProfileRequest,
    ): UpdateProfileResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            userRepository.updateUserPersonal(
                id = profileId,
                person = request.person,
            )
        }
        .map { newUser -> newUser.profile }
        .map(::UpdateProfileResponse)
        .single()

    override suspend fun updateProfileTypeById(
        authToken: String,
        profileId: String,
        request: UpdateProfileTypeRequest,
    ): UpdateProfileTypeResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            userRepository.updateUserProfileType(
                id = profileId,
                type = request.type,
            )
        }
        .map { newUser -> newUser.profile }
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

    override suspend fun deletePushTokenById(
        authToken: String,
        profileId: String,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.updateUserPushToken(
                    id = profileId,
                    pushToken = emptyString(),
                )
            }
            .map { }
            .single()
    }

    override suspend fun updateUserGroupAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserGroupsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.updateUserGroupAdd(
                    id = profileId,
                    groups = request.ids,
                )
            }
            .single()
    }

    override suspend fun updateUserGroupRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserGroupsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.updateUserGroupRemove(
                    id = profileId,
                    groups = request.ids,
                )
            }
            .single()
    }


    override suspend fun updateUserCommunicationsAddresseesGroupAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .map { targetUser ->
                        targetUser?.communications ?: throw HttpException(HttpStatusCode.NotFound)
                    }
                    .flatMapLatest { communications ->
                        userRepository.updateUserCommunications(
                            id = profileId,
                            communications = communications.copy(
                                availableAddressGroups = (communications.availableAddressGroups + request.ids).distinct(),
                            )
                        )
                    }

            }
            .single()
    }

    override suspend fun updateUserCommunicationsAddresseesGroupRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .map { targetUser ->
                        targetUser?.communications ?: throw HttpException(HttpStatusCode.NotFound)
                    }
                    .flatMapLatest { communications ->
                        userRepository.updateUserCommunications(
                            id = profileId,
                            communications = communications.copy(
                                availableAddressGroups = (communications.availableAddressGroups - request.ids.toSet()).distinct(),
                            )
                        )
                    }

            }
            .single()
    }

    override suspend fun updateUserCommunicationsAddresseesUserAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .map { targetUser ->
                        targetUser?.communications ?: throw HttpException(HttpStatusCode.NotFound)
                    }
                    .flatMapLatest { communications ->
                        userRepository.updateUserCommunications(
                            id = profileId,
                            communications = communications.copy(
                                availableAddressUsers = (communications.availableAddressUsers + request.ids).distinct(),
                            )
                        )
                    }

            }
            .single()
    }

    override suspend fun updateUserCommunicationsAddresseesUserRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .map { targetUser ->
                        targetUser?.communications ?: throw HttpException(HttpStatusCode.NotFound)
                    }
                    .flatMapLatest { communications ->
                        userRepository.updateUserCommunications(
                            id = profileId,
                            communications = communications.copy(
                                availableAddressUsers = (communications.availableAddressUsers - request.ids.toSet()).distinct(),
                            )
                        )
                    }

            }
            .single()
    }

    override suspend fun updateUserCommunicationsContactsAdd(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .map { targetUser ->
                        targetUser?.communications ?: throw HttpException(HttpStatusCode.NotFound)
                    }
                    .flatMapLatest { communications ->
                        userRepository.updateUserCommunications(
                            id = profileId,
                            communications = communications.copy(
                                availableContacts = (communications.availableContacts + request.ids).distinct(),
                            )
                        )
                    }

            }
            .single()
    }

    override suspend fun updateUserCommunicationsContactsRemove(
        authToken: String,
        profileId: String,
        request: UpdateUserCommunicationsRequest,
    ) {
        userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                userRepository.getUserById(profileId)
                    .map { targetUser ->
                        targetUser?.communications ?: throw HttpException(HttpStatusCode.NotFound)
                    }
                    .flatMapLatest { communications ->
                        userRepository.updateUserCommunications(
                            id = profileId,
                            communications = communications.copy(
                                availableContacts = (communications.availableContacts - request.ids.toSet()).distinct(),
                            )
                        )
                    }

            }
            .single()
    }


}