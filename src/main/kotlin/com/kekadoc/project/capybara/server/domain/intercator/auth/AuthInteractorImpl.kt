package com.kekadoc.project.capybara.server.domain.intercator.auth

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.secure.Hash
import com.kekadoc.project.capybara.server.common.secure.UserSalt
import com.kekadoc.project.capybara.server.data.repository.auth.AuthorizationRepository
import com.kekadoc.project.capybara.server.data.repository.auth.RefreshTokenValidation
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.domain.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.domain.intercator.requireUser
import com.kekadoc.project.capybara.server.domain.model.*
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationRequest
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationStatus
import com.kekadoc.project.capybara.server.domain.model.auth.registration.UpdateRegistrationStatusRequest
import com.kekadoc.project.capybara.server.routing.api.auth.model.*
import io.ktor.http.*
import kotlinx.coroutines.flow.*
import java.util.*

class AuthInteractorImpl(
    private val authorizationRepository: AuthorizationRepository,
    private val usersRepository: UsersRepository,
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
) : AuthInteractor {

    override suspend fun authorize(
        request: AuthorizationRequest,
    ): AuthorizationResponse {
        val user = usersRepository.findUserByLogin(
            login = request.login,
        ).requireUser().single()
        val hashedPassword = Hash.hash(
            value = request.password, salt = UserSalt.get(
                id = user.id,
                login = user.login,
            )
        )
        if (hashedPassword != user.password) {
            throw HttpException(statusCode = HttpStatusCode.Unauthorized, "bad cred")
        }
        val tokens = authorizationRepository.authorizeUser(user).single()

        return AuthorizationResponse(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
        )
    }

    override suspend fun refreshToken(
        request: RefreshTokensRequest,
    ): AuthorizationResponse {
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
                AuthorizationResponse(
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
        val res = authorizationRepository.registration(req).single()
        return RegistrationStatusResponseDto(
            id = res.id,
            status = RegistrationStatusDto.valueOf(res.status.name)
        )
    }

    override suspend fun getRegistrationStatus(
        registrationId: Identifier,
    ): RegistrationStatusResponseDto {
        val res = authorizationRepository.getRegistrationStatus(registrationId).single()
        return RegistrationStatusResponseDto(
            id = res.id,
            status = RegistrationStatusDto.valueOf(res.status.name)
        )
    }

    override suspend fun cancelRegistrationRequest(
        registrationId: Identifier,
    ): RegistrationStatusResponseDto {
        val res = authorizationRepository.updateRegistrationStatus(
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
                authorizationRepository.updateRegistrationStatus(
                    registrationId = registrationId,
                    request = UpdateRegistrationStatusRequest(
                        status = RegistrationStatus.valueOf(request.status.name)
                    )
                )
            }
            .onEach { requestInfo ->
                if (requestInfo.status == RegistrationStatus.COMPLETED) {
                    val createdUser = usersRepository.createUser(
                        login = "Oleg1234",
                        password = "1234",
                        profile = Profile(
                            type = Profile.Type.USER,
                            name = requestInfo.name,
                            surname = requestInfo.surname,
                            patronymic = requestInfo.patronymic,
                            about = if (requestInfo.isStudent) "Студент" else null
                        )
                    ).single()
                    usersRepository.updateUserCommunications(
                        userId = createdUser.id,
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
            .map {
                UpdateRegistrationStatusResponseDto(
                    status = RegistrationStatusDto.valueOf(request.status.name)
                )
            }
            .single()
    }

    override suspend fun getAllRegistrationRequests(
        authToken: Token,
    ): GetAllRegistrationRequestsResponseDto {
        return fetchUserByAccessTokenFunction.fetchUser(authToken).requireAdminUser()
            .flatMapConcat { authorizationRepository.getAllRegistrationRequests() }.map {
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

    override suspend fun registrationConfirmEmail(registrationId: Identifier) {
        authorizationRepository.updateRegistrationStatus(
            registrationId = registrationId,
            request = UpdateRegistrationStatusRequest(
                status = RegistrationStatus.WAIT_APPROVING,
            )
        ).collect()
    }

}