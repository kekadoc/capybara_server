package com.kekadoc.project.capybara.server.domain.intercator.profile

import com.kekadoc.project.capybara.server.domain.model.Token

interface ProfileInteractor {

    suspend fun confirmEmail(
        confirmationToken: Token,
    ): String

}