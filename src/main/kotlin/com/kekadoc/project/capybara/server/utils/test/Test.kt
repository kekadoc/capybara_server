package com.kekadoc.project.capybara.server.utils.test

import com.kekadoc.project.capybara.server.common.component.Component
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.model.Range
import io.ktor.server.application.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import org.koin.core.component.get
import java.util.*

object Test : Component {

    override fun init(application: Application) {

        if (true) {
            GlobalScope.launch {
                val usersRepository = Di.get<UsersRepository>()
                val groupsRepository = Di.get<GroupsRepository>()
                val messagesRepository = Di.get<MessagesRepository>()

//                usersRepository.createUser(
//                    login = "OlegAdmin",
//                    password = "123",
//                    profile = Profile(
//                        type = Profile.Type.ADMIN,
//                        name = "Oleg",
//                        surname = "Korelin",
//                        patronymic = "Sergeervich",
//                        about = null,
//                    ),
//                ).collect()

                messagesRepository.getMessagesByAuthorId(
                    UUID.fromString("a477e8db-4269-4dbb-806c-8b65d1e5289d"),
                    Range(0, 100),
                )
                    .flatMapConcat { messages ->
                        messages.map { message ->
                            messagesRepository.removeMessage(message.id)
                        }.merge()
                    }
                    .collect()

            }
        }

    }

}