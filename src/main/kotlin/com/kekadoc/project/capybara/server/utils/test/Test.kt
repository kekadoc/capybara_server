package com.kekadoc.project.capybara.server.utils.test

import com.kekadoc.project.capybara.server.common.component.Component
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.model.Profile
import io.ktor.server.application.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import org.koin.core.component.get

object Test : Component {

    override fun init(application: Application) {

        if (false) {
            Di.get<UsersRepository>().apply {
                this.createUser(
                    login = "OlegAdmin",
                    password = "123",
                    profile = Profile(
                        type = Profile.Type.ADMIN,
                        name = "Oleg",
                        surname = "Korelin",
                        patronymic = "Sergeervich",
                        about = null,
                    ),
                ).launchIn(GlobalScope)
            }
        }

    }

}