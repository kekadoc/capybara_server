package com.kekadoc.project.capybara.server.data.function.create_user

import com.kekadoc.project.capybara.server.common.secure.PasswordGenerator
import com.kekadoc.project.capybara.server.common.tools.LoginGenerator
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.domain.model.user.CreatedUser
import com.kekadoc.project.capybara.server.domain.model.user.Profile
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class CreateUserFunctionImpl(
    private val usersRepository: UsersRepository,
) : CreateUserFunction {

    override suspend fun invoke(profile: Profile, login: String?, password: String?): CreatedUser {
        val correctedLogin = login ?: LoginGenerator.generate(
            surname = profile.surname,
            name = profile.name,
            patronymic = profile.patronymic,
        )
        val correctedPassword = password ?: PasswordGenerator.generate()
        return usersRepository.createUser(
            login = correctedLogin,
            password = correctedPassword,
            profile = Profile(
                type = profile.type,
                name = profile.name,
                surname = profile.surname,
                patronymic = profile.patronymic,
                about = profile.about,
            ),
        ).map { user ->
            CreatedUser(
                user = user,
                tempPass = correctedPassword,
            )
        }.single()
    }

}