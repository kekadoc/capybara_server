package com.kekadoc.project.capybara.server.data.service.email

import com.kekadoc.project.capybara.server.domain.model.Identifier

interface EmailDataService {

    suspend fun sentEmailWithLoginEndTempPassword(
        email: String,
        name: String,
        patronymic: String,
        login: String,
        password: String,
    )

    suspend fun sentConfirmEmail(
        registrationId: Identifier,
        email: String,
    )

}