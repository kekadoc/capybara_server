package com.kekadoc.project.capybara.server.utils.test

import com.kekadoc.project.capybara.server.common.component.Component
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.model.Profile
import io.ktor.server.application.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.get

object Test : Component {

    override fun init(application: Application) {

        if (false) {
            GlobalScope.launch {
                val usersRepository = Di.get<UsersRepository>()
                val groupsRepository = Di.get<GroupsRepository>()
                val messagesRepository = Di.get<MessagesRepository>()

                usersRepository.createUser(
                    login = "OlegAdmin",
                    password = "123",
                    profile = Profile(
                        type = Profile.Type.ADMIN,
                        name = "Oleg",
                        surname = "Korelin",
                        patronymic = "Sergeervich",
                        about = null,
                    ),
                ).collect()
            }
        }

    }

}