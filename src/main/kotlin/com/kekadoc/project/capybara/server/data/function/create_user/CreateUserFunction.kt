package com.kekadoc.project.capybara.server.data.function.create_user

import com.kekadoc.project.capybara.server.domain.model.user.CreatedUser
import com.kekadoc.project.capybara.server.domain.model.user.Profile

interface CreateUserFunction {

    suspend operator fun invoke(
        profile: Profile,
        login: String? = null,
        password: String? = null,
    ): CreatedUser

}