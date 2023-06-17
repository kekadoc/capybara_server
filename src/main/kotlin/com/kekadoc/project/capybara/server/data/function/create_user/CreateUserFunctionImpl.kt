package com.kekadoc.project.capybara.server.data.function.create_user

import com.kekadoc.project.capybara.server.common.secure.PasswordGenerator
import com.kekadoc.project.capybara.server.common.tools.LoginGenerator
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.domain.model.user.CreatedUser
import com.kekadoc.project.capybara.server.domain.model.user.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class CreateUserFunctionImpl(
    private val usersRepository: UsersRepository,
) : CreateUserFunction {

    override suspend fun invoke(
        type: Profile.Type,
        name: String,
        surname: String,
        patronymic: String?,
        about: String?,
        login: String?,
        password: String?
    ): Flow<CreatedUser> = flowOf {
        val correctedLogin = login ?: LoginGenerator.generate(
            surname = surname,
            name = name,
            patronymic = patronymic,
        )
        val correctedPassword = password ?: PasswordGenerator.generate()
        usersRepository.createUser(
            login = correctedLogin,
            password = correctedPassword,
            profile = Profile(
                type = type,
                name = name,
                surname = surname,
                patronymic = patronymic,
                about = about,
            ),
        )
            .map { user -> CreatedUser(user = user, tempPass = correctedPassword) }
    }.flattenConcat()

}