package com.kekadoc.project.capybara.server.data.manager.registration

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.auth.registration.GetAllRegistrationRequestsResponse
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationRequest
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationRequestInfo
import com.kekadoc.project.capybara.server.domain.model.auth.registration.UpdateRegistrationStatusRequest
import kotlinx.coroutines.flow.Flow

interface RegistrationManager {

    /**
     *
     */
    fun registration(request: RegistrationRequest): Flow<RegistrationRequestInfo>

    /**
     *
     */
    fun getRegistrationStatus(registrationId: Identifier): Flow<RegistrationRequestInfo>

    /**
     *
     */
    fun updateRegistrationStatus(
        registrationId: Identifier,
        request: UpdateRegistrationStatusRequest,
    ): Flow<RegistrationRequestInfo>

    /**
     *
     */
    fun getAllRegistrationRequests(): Flow<GetAllRegistrationRequestsResponse>

}