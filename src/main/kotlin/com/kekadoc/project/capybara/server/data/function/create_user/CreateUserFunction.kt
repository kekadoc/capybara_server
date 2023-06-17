package com.kekadoc.project.capybara.server.data.function.create_user

import com.kekadoc.project.capybara.server.domain.model.user.CreatedUser
import com.kekadoc.project.capybara.server.domain.model.user.Profile
import kotlinx.coroutines.flow.Flow

interface CreateUserFunction {

    suspend operator fun invoke(
        type: Profile.Type,
        name: String,
        surname: String,
        patronymic: String? = null,
        about: String? = null,
        login: String? = null,
        password: String? = null,
    ): Flow<CreatedUser>

}