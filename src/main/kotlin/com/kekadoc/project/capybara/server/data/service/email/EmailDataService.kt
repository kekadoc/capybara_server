package com.kekadoc.project.capybara.server.data.service.email

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.user.User

interface EmailDataService {

    suspend fun sentEmailWithLoginEndTempPassword(
        email: String,
        name: String,
        patronymic: String?,
        login: String,
        password: String,
    )

    suspend fun sentConfirmEmail(
        registrationId: Identifier,
        email: String,
    )

    suspend fun sentConfirmEmail(
        user: User,
        email: String,
    )

    suspend fun checkEmailConfirmation(token: Token): EmailConfirmation?

    data class EmailConfirmation(
        val id: Identifier,
        val email: String,
    )

}