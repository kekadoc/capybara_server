@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package com.kekadoc.project.capybara.server.intercator.profile

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.model.*
import com.kekadoc.project.capybara.server.data.model.access.UserAccessToGroup
import com.kekadoc.project.capybara.server.data.model.access.UserAccessToUser
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.converter.dto.ProfileTypeDtoConverter
import com.kekadoc.project.capybara.server.data.source.factory.dto.AuthorizedUserDtoFactory
import com.kekadoc.project.capybara.server.data.source.factory.dto.ProfileDtoFactory
import com.kekadoc.project.capybara.server.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.profile.model.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileInteractorImpl(
    private val userRepository: UsersRepository,
    //private val mobileNotificationsRepository: MobileNotificationsRepository,
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
) : ProfileInteractor {

    override suspend fun createProfile(
        authToken: Token,
        request: CreateProfileRequest,
    ): CreateProfileResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            userRepository.getUserByLogin(request.login)
                .flatMapLatest { userByLogin ->
                    if (userByLogin != null) {
                        throw HttpException(
                            statusCode = HttpStatusCode.Conflict,
                            message = "User with login already exist",
                        )
                    } else {
                        userRepository.createUser(
                            login = request.login,
                            password = request.password,
                            profile = Profile(
                                type = ProfileTypeDtoConverter.convert(request.type),
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
        .map(ProfileDtoFactory::create)
        .map(::CreateProfileResponse)
        .single()

    override suspend fun getProfileByAuthToken(
        authToken: Token,
    ): GetAuthorizedProfileResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .map(AuthorizedUserDtoFactory::create)
        .map(::GetAuthorizedProfileResponse)
        .single()

    override suspend fun updateProfileByAuthToken(
        authToken: Token,
        request: UpdateProfileRequest,
    ): UpdateProfileResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
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
            ).map { newUser -> newUser ?: throw HttpException(HttpStatusCode.NotFound) }
        }
        .map(ProfileDtoFactory::create)
        .map(::UpdateProfileResponse)
        .single()

    override suspend fun getProfileById(
        authToken: Token,
        profileId: Identifier,
    ): GetProfileResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .flatMapLatest { user ->
            if (user.id == profileId) {
                flowOf(true)
            } else {
                userRepository.getAccessForUser(
                    userId = profileId,
                    forUserId = user.id,
                ).map { access -> access?.readProfile != true }
            }
        }
        .flatMapLatest { readProfile ->
            if (readProfile) userRepository.getUserById(profileId)
            else throw HttpException(HttpStatusCode.Forbidden)
        }
        .map { user -> user ?: throw HttpException(HttpStatusCode.NotFound) }
        .map(ProfileDtoFactory::create)
        .map(::GetProfileResponse)
        .single()

    override suspend fun updateProfileById(
        authToken: Token,
        profileId: Identifier,
        request: UpdateProfileRequest,
    ): UpdateProfileResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
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
        .map { newUser -> newUser ?: throw HttpException(HttpStatusCode.NotFound) }
        .map(ProfileDtoFactory::create)
        .map(::UpdateProfileResponse)
        .single()

    override suspend fun updateProfileTypeById(
        authToken: Token,
        profileId: Identifier,
        request: UpdateProfileTypeRequest,
    ): UpdateProfileTypeResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { userRepository.getUserById(profileId) }
        .map { targetUser -> targetUser ?: throw HttpException(HttpStatusCode.NotFound) }
        .flatMapLatest { targetUser ->
            userRepository.updateUserProfile(
                userId = targetUser.id,
                profile = targetUser.profile.copy(
                    type = ProfileTypeDtoConverter.convert(request.type)
                ),
            )
        }
        .map { newUser -> newUser ?: throw HttpException(HttpStatusCode.NotFound) }
        .map(ProfileDtoFactory::create)
        .map(::UpdateProfileTypeResponse)
        .single()

    override suspend fun deleteProfileById(
        authToken: Token,
        profileId: Identifier,
    ): Unit = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { userRepository.deleteUser(profileId) }
        .onEach { deletedUser ->
            if (deletedUser == null) throw HttpException(HttpStatusCode.NotFound)
        }
        .collect()

    override suspend fun getAccessUser(
        authToken: Token,
        fromUserId: Identifier,
        toUserId: Identifier,
    ): GetAccessUserResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { authorizedUser ->
            if (fromUserId == authorizedUser.id || authorizedUser.profile.type == Profile.Type.ADMIN) {
                userRepository.getAccessForUser(
                    userId = fromUserId,
                    forUserId = toUserId,
                )
            } else {
                throw HttpException(HttpStatusCode.Forbidden)
            }
        }
        .map { access ->
            access ?: UserAccessToUser.nothing(
                fromUserId = fromUserId,
                toUserId = toUserId,
            )
        }
        .map { access ->
            GetAccessUserResponseDto(
                readProfile = access.readProfile,
                sentNotification = access.sentNotification,
                contactInfo = access.contactInfo,
            )
        }
        .single()

    override suspend fun updateAccessUser(
        authToken: Token,
        fromUserId: Identifier,
        toUserId: Identifier,
        request: UpdateAccessUserRequestDto,
    ): UpdateAccessUserResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            userRepository.updateAccessForUser(
                userId = fromUserId,
                forUserId = toUserId,
                userAccessUser = UserAccessToUser.Updater(
                    readProfile = request.readProfile,
                    sentNotification = request.sentNotification,
                    contactInfo = request.contactInfo,
                ),
            )
        }
        .map { access -> access ?: throw HttpException(HttpStatusCode.NotFound) }
        .map { access ->
            UpdateAccessUserResponseDto(
                readProfile = access.readProfile,
                sentNotification = access.sentNotification,
                contactInfo = access.contactInfo,
            )
        }
        .single()

    override suspend fun getAccessGroup(
        authToken: Token,
        userId: Identifier,
        groupId: Identifier,
    ): GetAccessGroupResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest {
            userRepository.getAccessForGroup(
                userId = userId,
                groupId = groupId,
            )
        }
        .map { access -> access ?: throw HttpException(HttpStatusCode.NotFound) }
        .map { access ->
            GetAccessGroupResponseDto(
                readInfo = access.readInfo,
                readMembers = access.readMembers,
                sentNotification = access.sentNotification,
            )
        }
        .single()

    override suspend fun updateAccessGroup(
        authToken: Token,
        userId: Identifier,
        groupId: Identifier,
        request: UpdateAccessGroupRequestDto,
    ): UpdateAccessGroupResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest {
            userRepository.updateAccessForGroup(
                userId = userId,
                groupId = groupId,
                userAccessGroup = UserAccessToGroup.Updater(
                    readInfo = request.readInfo,
                    readMembers = request.readMembers,
                    sentNotification = request.sentNotification,
                ),
            )
        }
        .map { access -> access ?: throw HttpException(HttpStatusCode.NotFound) }
        .map { access ->
            UpdateAccessGroupResponseDto(
                readInfo = access.readInfo,
                readMembers = access.readMembers,
                sentNotification = access.sentNotification,
            )
        }
        .single()

    override suspend fun updateCommunications(
        authToken: Token,
        request: UpdateUserCommunicationsRequest,
    ): UpdateUserCommunicationsResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            userRepository.updateUserCommunications(
                userId = user.id,
                communications = Communications(
                    values = request.values.map { (type, value) ->
                        Communication(
                            type = type,
                            value = value,
                        )
                    }
                ),
            )
        }
        .map { access -> access ?: throw HttpException(HttpStatusCode.NotFound) }
        .map(AuthorizedUserDtoFactory::create)
        .map(::UpdateUserCommunicationsResponseDto)
        .single()

    override suspend fun updateCommunications(
        authToken: Token,
        profileId: Identifier,
        request: UpdateUserCommunicationsRequest,
    ): UpdateUserCommunicationsResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            userRepository.updateUserCommunications(
                userId = profileId,
                communications = Communications(
                    values = request.values.map { (type, value) ->
                        Communication(
                            type = type,
                            value = value,
                        )
                    }
                ),
            )
        }
        .map { access -> access ?: throw HttpException(HttpStatusCode.NotFound) }
        .map(AuthorizedUserDtoFactory::create)
        .map(::UpdateUserCommunicationsResponseDto)
        .single()

}