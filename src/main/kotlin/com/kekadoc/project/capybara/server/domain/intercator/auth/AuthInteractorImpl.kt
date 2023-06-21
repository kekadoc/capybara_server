package com.kekadoc.project.capybara.server.domain.intercator.auth

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.secure.Hash
import com.kekadoc.project.capybara.server.common.secure.UserSalt
import com.kekadoc.project.capybara.server.data.function.create_user.CreateUserFunction
import com.kekadoc.project.capybara.server.data.manager.registration.RegistrationManager
import com.kekadoc.project.capybara.server.data.repository.auth.AuthorizationRepository
import com.kekadoc.project.capybara.server.data.repository.auth.RefreshTokenValidation
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.service.email.EmailDataService
import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.domain.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.domain.intercator.requireUser
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationRequest
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationStatus
import com.kekadoc.project.capybara.server.domain.model.auth.registration.UpdateRegistrationStatusRequest
import com.kekadoc.project.capybara.server.domain.model.group.Group
import com.kekadoc.project.capybara.server.domain.model.user.Communication
import com.kekadoc.project.capybara.server.domain.model.user.Communications
import com.kekadoc.project.capybara.server.domain.model.user.Profile
import com.kekadoc.project.capybara.server.routing.api.auth.model.*
import com.kekadoc.project.capybara.server.routing.api.auth.model.factory.RegistrationRequestInfoDtoFactory
import io.ktor.http.*
import kotlinx.coroutines.flow.*
import java.util.*

class AuthInteractorImpl(
    private val registrationManager: RegistrationManager,
    private val authorizationRepository: AuthorizationRepository,
    private val usersRepository: UsersRepository,
    private val groupsRepository: GroupsRepository,
    private val emailDataService: EmailDataService,
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
    private val createUserFunction: CreateUserFunction,
) : AuthInteractor {

    override suspend fun authorize(
        request: AuthorizationRequestDto,
    ): AuthorizationResponseDto = usersRepository.findUserByLogin(login = request.login)
        .map { user ->
            user ?: throw HttpException(
                statusCode = HttpStatusCode.Unauthorized,
                message = "Bad credentials",
            )
        }
        .onEach { user ->
            val hashedPassword = Hash.hash(
                value = request.password,
                salt = UserSalt.get(
                    id = user.id,
                    login = user.login,
                )
            )
            if (hashedPassword != user.password) {
                throw HttpException(
                    statusCode = HttpStatusCode.Unauthorized,
                    message = "Bad credentials",
                )
            }
        }
        .flatMapConcat(authorizationRepository::authorizeUser)
        .map { (accessToken, refreshToken) ->
            AuthorizationResponseDto(
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        }
        .single()

    override suspend fun refreshToken(
        request: RefreshTokensRequestDto,
    ): AuthorizationResponseDto {
        return when (val validation = authorizationRepository.validateRefreshToken(request.refreshToken)
            .single()) {
            is RefreshTokenValidation.Expired -> {
                throw HttpException(statusCode = HttpStatusCode.Unauthorized, "Expired")
            }

            is RefreshTokenValidation.Invalid -> {
                throw HttpException(statusCode = HttpStatusCode.Unauthorized, "Invalid")
            }

            is RefreshTokenValidation.Valid -> {
                val userId = validation.userId
                val login = validation.login
                val user = combine(
                    usersRepository.findUserByLogin(
                        login = login,
                    ), usersRepository.getUserById(
                        id = UUID.fromString(userId.toString()),
                    )
                ) { userByLogin, userById ->
                    when {
                        userByLogin == null || userById == null -> {
                            throw HttpException(statusCode = HttpStatusCode.Unauthorized, "Invalid")
                        }

                        userByLogin.id != userById.id -> {
                            throw HttpException(statusCode = HttpStatusCode.Unauthorized, "Invalid")
                        }

                        else -> {
                            userById
                        }
                    }
                }.requireUser().single()
                val authorization = authorizationRepository.authorizeUser(user).single()
                AuthorizationResponseDto(
                    accessToken = authorization.accessToken,
                    refreshToken = authorization.refreshToken,
                )
            }
        }
    }

    override suspend fun registration(
        request: RegistrationRequestDto,
    ): RegistrationStatusResponseDto {
        val req = RegistrationRequest(
            name = request.name,
            surname = request.surname,
            patronymic = request.patronymic,
            email = request.email,
            isStudent = request.isStudent,
            groupId = request.groupId
        )
        val res = registrationManager.registration(req).single()
        return RegistrationStatusResponseDto(
            id = res.id,
            status = RegistrationStatusDto.valueOf(res.status.name)
        )
    }

    override suspend fun getRegistrationStatus(
        registrationId: Identifier,
    ): RegistrationStatusResponseDto {
        val res = registrationManager.getRegistrationStatus(registrationId).single()
        return RegistrationStatusResponseDto(
            id = res.id,
            status = RegistrationStatusDto.valueOf(res.status.name)
        )
    }

    override suspend fun cancelRegistrationRequest(
        registrationId: Identifier,
    ): RegistrationStatusResponseDto {
        val res = registrationManager.updateRegistrationStatus(
            registrationId = registrationId,
            request = UpdateRegistrationStatusRequest(RegistrationStatus.CANCELLED),
        ).single()
        return RegistrationStatusResponseDto(
            id = res.id,
            status = RegistrationStatusDto.valueOf(res.status.name)
        )
    }

    override suspend fun updateRegistrationStatus(
        authToken: Token,
        registrationId: Identifier,
        request: UpdateRegistrationStatusRequestDto,
    ): UpdateRegistrationStatusResponseDto {
        return fetchUserByAccessTokenFunction.fetchUser(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapConcat {
                registrationManager.updateRegistrationStatus(
                    registrationId = registrationId,
                    request = UpdateRegistrationStatusRequest(
                        status = RegistrationStatus.valueOf(request.status.name)
                    )
                )
            }
            .onEach { requestInfo ->
                if (requestInfo.status == RegistrationStatus.COMPLETED) {
                    val groupId = requestInfo.groupId
                    val group: Group? = if (groupId == null) null else {
                        groupsRepository.getGroup(groupId)
                            .nullable()
                            .catch { emit(null) }
                            .singleOrNull()
                    }
                    val createdUser = createUserFunction.invoke(
                        type = Profile.Type.USER,
                        name = requestInfo.name,
                        surname = requestInfo.surname,
                        patronymic = requestInfo.patronymic,
                        about = if (group != null) "Студент группы ${group.name}" else null,
                    ).single()
                    usersRepository.updateUserCommunications(
                        userId = createdUser.user.id,
                        communications = Communications(
                            values = listOf(
                                Communication(
                                    type = Communication.Type.Email,
                                    value = requestInfo.email,
                                    approved = true,
                                )
                            ),
                        )
                    ).collect()
                }
            }
            .map(RegistrationRequestInfoDtoFactory::create)
            .map(::UpdateRegistrationStatusResponseDto)
            .single()
    }

    override suspend fun getAllRegistrationRequests(
        authToken: Token,
    ): GetAllRegistrationRequestsResponseDto {
        return fetchUserByAccessTokenFunction.fetchUser(authToken).requireAdminUser()
            .flatMapConcat { registrationManager.getAllRegistrationRequests() }.map {
                GetAllRegistrationRequestsResponseDto(
                    items = it.items.map {
                        RegistrationRequestInfoDto(
                            id = it.id,
                            status = RegistrationStatusDto.valueOf(it.status.name),
                            name = it.name,
                            surname = it.surname,
                            patronymic = it.patronymic,
                            email = it.email,
                            isStudent = it.isStudent,
                            groupId = it.groupId,
                        )
                    },
                )
            }.single()
    }

    override suspend fun registrationConfirmEmail(token: Token): String {
        val confirmResult = emailDataService.checkEmailConfirmation(token)
            ?: throw HttpException(HttpStatusCode.BadRequest)
        return registrationManager.updateRegistrationStatus(
            registrationId = confirmResult.id,
            request = UpdateRegistrationStatusRequest(
                status = RegistrationStatus.WAIT_APPROVING,
            )
        )
            .map { "Электронная почта успешно подтверждена ✅.\nПожалуйста, ожидайте подтверждения заявки администратором." }
            .single()
    }

}