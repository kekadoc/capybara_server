@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.kekadoc.project.capybara.server.domain.intercator.profile

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.function.create_user.CreateUserFunction
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.service.email.EmailDataService
import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.domain.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.domain.intercator.requireUser
import com.kekadoc.project.capybara.server.domain.model.*
import com.kekadoc.project.capybara.server.domain.model.user.*
import com.kekadoc.project.capybara.server.routing.api.profile.model.*
import com.kekadoc.project.capybara.server.routing.model.converter.ProfileTypeDtoConverter
import com.kekadoc.project.capybara.server.routing.model.factory.ExtendedProfileDtoFactory
import com.kekadoc.project.capybara.server.routing.model.factory.ProfileDtoFactory
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class ProfileAdminInteractorImpl(
    private val userRepository: UsersRepository,
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
    private val createUserFunction: CreateUserFunction,
    private val emailDataService: EmailDataService,
) : ProfileAdminInteractor {

    override suspend fun createProfile(
        adminAccessToken: Token,
        request: CreateProfileRequestDto,
    ): CreateProfileResponseDto = fetchUserByAccessTokenFunction.fetchUser(adminAccessToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            if (request.login != null) {
                userRepository.findUserByLogin(request.login)
                    .onEach { userByLogin ->
                        if (userByLogin != null) {
                            throw HttpException(
                                statusCode = HttpStatusCode.Conflict,
                                message = "User with login already exist",
                            )
                        }
                    }
                    .map { Unit }
            } else {
                flowOf(Unit)
            }
        }
        .map {
            createUserFunction.invoke(
                Profile(
                    type = ProfileTypeDtoConverter.convert(request.type),
                    name = request.name,
                    surname = request.surname,
                    patronymic = request.patronymic,
                    about = request.about,
                ),
                login = request.login,
                password = request.password,
            )
        }
        .onEach { (user, pass) ->
            request.emailForInvite?.takeIf { it.isNotBlank() }?.also { email ->
                emailDataService.sentEmailWithLoginEndTempPassword(
                    email = email,
                    name = user.profile.name,
                    patronymic = user.profile.patronymic,
                    login = user.login,
                    password = pass,
                )
            }
        }
        .map { (user, password) ->
            CreateProfileResponseDto(
                profile = ExtendedProfileDtoFactory.create(user),
                tempPassword = password,
            )
        }
        .onEach {
        }
        .single()

    override suspend fun getExtendedProfile(
        adminAccessToken: Token,
        profileId: Identifier,
    ): GetExtendedProfileResponseDto = fetchUserByAccessTokenFunction.fetchUser(adminAccessToken)
        .requireAdminUser()
        .flatMapConcat { userRepository.getUserById(profileId) }
        .requireUser()
        .map(ExtendedProfileDtoFactory::create)
        .map(::GetExtendedProfileResponseDto)
        .single()

    override suspend fun updateProfilePersonal(
        adminAccessToken: Token,
        profileId: Identifier,
        request: UpdateProfileRequestDto,
    ): UpdateProfileResponseDto = fetchUserByAccessTokenFunction.fetchUser(adminAccessToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { user ->
            userRepository.updateUserProfile(
                userId = profileId,
                profile = user.profile.copy(
                    name = request.name,
                    surname = request.surname,
                    patronymic = request.patronymic,
                    about = request.about,
                ),
            )
        }
        .requireUser()
        .map(ExtendedProfileDtoFactory::create)
        .map(::UpdateProfileResponseDto)
        .single()

    override suspend fun updateProfileStatus(
        adminAccessToken: Token,
        profileId: Identifier,
        request: UpdateProfileStatusRequestDto,
    ): UpdateProfileStatusResponseDto = fetchUserByAccessTokenFunction.fetchUser(adminAccessToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { userRepository.getUserById(profileId) }
        .requireUser()
        .flatMapLatest { targetUser ->
            val status = UserStatus.values().find { it.name == request.status }
                ?: throw HttpException(HttpStatusCode.BadRequest, "Unknown status")
            userRepository.updateUserStatus(
                userId = targetUser.id,
                status = status,
            )
        }
        .requireUser()
        .map(ExtendedProfileDtoFactory::create)
        .map(::UpdateProfileStatusResponseDto)
        .single()

    override suspend fun updateProfileType(
        adminAccessToken: Token,
        profileId: Identifier,
        request: UpdateProfileTypeRequestDto,
    ): UpdateProfileTypeResponseDto = fetchUserByAccessTokenFunction.fetchUser(adminAccessToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { userRepository.getUserById(profileId) }
        .requireUser()
        .flatMapLatest { targetUser ->
            userRepository.updateUserProfile(
                userId = targetUser.id,
                profile = targetUser.profile.copy(
                    type = ProfileTypeDtoConverter.convert(request.type)
                ),
            )
        }
        .requireUser()
        .map(ProfileDtoFactory::create)
        .map(::UpdateProfileTypeResponseDto)
        .single()

    override suspend fun updateProfilePassword(
        adminAccessToken: Token,
        profileId: Identifier,
        request: UpdateProfilePasswordRequestDto,
    ): UpdateProfilePasswordResponseDto = fetchUserByAccessTokenFunction.fetchUser(adminAccessToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            userRepository.updateUserPassword(
                userId = profileId,
                newPassword = request.password,
            )
        }
        .map(ExtendedProfileDtoFactory::create)
        .map(::UpdateProfilePasswordResponseDto)
        .single()

    override suspend fun deleteProfile(
        adminAccessToken: Token,
        profileId: Identifier,
    ): DeleteProfileResponseDto = fetchUserByAccessTokenFunction.fetchUser(adminAccessToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { userRepository.deleteUser(profileId) }
        .swap(::DeleteProfileResponseDto)
        .single()

    override suspend fun getAccessUser(
        adminAccessToken: Token,
        fromUserId: Identifier,
        toUserId: Identifier,
    ): GetAccessUserResponseDto = fetchUserByAccessTokenFunction.fetchUser(adminAccessToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            userRepository.getAccessForUser(
                userId = fromUserId,
                forUserId = toUserId,
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
        adminAccessToken: Token,
        fromUserId: Identifier,
        toUserId: Identifier,
        request: UpdateAccessUserRequestDto,
    ): UpdateAccessUserResponseDto = fetchUserByAccessTokenFunction.fetchUser(adminAccessToken)
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
        .map { access ->
            UpdateAccessUserResponseDto(
                readProfile = access.readProfile,
                sentNotification = access.sentNotification,
                contactInfo = access.contactInfo,
            )
        }
        .single()

    override suspend fun getAccessGroup(
        adminAccessToken: Token,
        userId: Identifier,
        groupId: Identifier,
    ): GetAccessGroupResponseDto = fetchUserByAccessTokenFunction.fetchUser(adminAccessToken)
        .requireAuthorizedUser()
        .flatMapLatest {
            userRepository.getAccessForGroup(
                userId = userId,
                groupId = groupId,
            )
        }
        .map { access ->
            GetAccessGroupResponseDto(
                readInfo = access.readInfo,
                readMembers = access.readMembers,
                sentNotification = access.sentNotification,
            )
        }
        .single()

    override suspend fun updateAccessGroup(
        adminAccessToken: Token,
        userId: Identifier,
        groupId: Identifier,
        request: UpdateAccessGroupRequestDto,
    ): UpdateAccessGroupResponseDto = fetchUserByAccessTokenFunction.fetchUser(adminAccessToken)
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
        .map { access ->
            UpdateAccessGroupResponseDto(
                readInfo = access.readInfo,
                readMembers = access.readMembers,
                sentNotification = access.sentNotification,
            )
        }
        .single()

    override suspend fun updateProfileCommunications(
        adminAccessToken: Token,
        profileId: Identifier,
        request: UpdateUserCommunicationsRequest,
    ): UpdateUserCommunicationsResponseDto = fetchUserByAccessTokenFunction.fetchUser(adminAccessToken)
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