package com.kekadoc.project.capybara.server.data.manager.registration

import com.kekadoc.project.capybara.server.data.function.create_user.CreateUserFunction
import com.kekadoc.project.capybara.server.data.repository.auth.AuthorizationRepository
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.service.email.EmailDataService
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Profile
import com.kekadoc.project.capybara.server.domain.model.auth.registration.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single

class RegistrationManagerImpl(
    private val authorizationRepository: AuthorizationRepository,
    private val usersRepository: UsersRepository,
    private val groupsRepository: GroupsRepository,
    private val emailDataService: EmailDataService,
    private val createUserFunction: CreateUserFunction,
) : RegistrationManager {

    override fun registration(request: RegistrationRequest): Flow<RegistrationRequestInfo> {
        return authorizationRepository.registration(request)
            .onEach { newRequest ->
                emailDataService.sentConfirmEmail(
                    registrationId = newRequest.id,
                    email = request.email,
                )
            }
    }

    override fun getRegistrationStatus(registrationId: Identifier): Flow<RegistrationRequestInfo> {
        return authorizationRepository.getRegistrationStatus(registrationId)
    }

    override fun updateRegistrationStatus(
        registrationId: Identifier,
        request: UpdateRegistrationStatusRequest,
    ): Flow<RegistrationRequestInfo> = authorizationRepository.updateRegistrationStatus(
        registrationId = registrationId,
        request = request,
    )
        .map { regRequest ->
            if (regRequest.status == RegistrationStatus.COMPLETED) {
                val about = if (regRequest.isStudent) {
                    val group = regRequest.groupId?.let(groupsRepository::getGroup)?.single()
                    if (group != null) {
                        "Студент группы ${group.name}"
                    } else {
                        "Студент"
                    }
                } else null
                val user = createUserFunction.invoke(
                    profile = Profile(
                        type = Profile.Type.USER,
                        name = regRequest.name,
                        surname = regRequest.surname,
                        patronymic = regRequest.patronymic,
                        about = about,
                    )
                )
                if (regRequest.email.isNotBlank()) {
                    emailDataService.sentEmailWithLoginEndTempPassword(
                        email = regRequest.email,
                        name = user.user.profile.name,
                        patronymic = user.user.profile.patronymic,
                        login = user.user.login,
                        password = user.tempPass,
                    )
                }
            }
            regRequest
        }

    override fun getAllRegistrationRequests(): Flow<GetAllRegistrationRequestsResponse> {
        return authorizationRepository.getAllRegistrationRequests()
    }

}