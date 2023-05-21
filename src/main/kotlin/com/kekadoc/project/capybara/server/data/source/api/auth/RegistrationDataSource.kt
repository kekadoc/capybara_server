package com.kekadoc.project.capybara.server.data.source.api.auth

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.auth.registration.GetAllRegistrationRequestsResponse
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationRequest
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationRequestInfo
import com.kekadoc.project.capybara.server.domain.model.auth.registration.UpdateRegistrationStatusRequest

interface RegistrationDataSource {

    suspend fun registration(request: RegistrationRequest): RegistrationRequestInfo

    suspend fun getRegistrationStatus(registrationId: Identifier): RegistrationRequestInfo

    suspend fun updateRegistrationStatus(
        registrationId: Identifier,
        request: UpdateRegistrationStatusRequest,
    ): RegistrationRequestInfo

    suspend fun getAllRegistrationRequests(): GetAllRegistrationRequestsResponse

}