package com.kekadoc.project.capybara.server.utils.test

import com.kekadoc.project.capybara.server.common.component.Component
import io.ktor.server.application.*

object Test : Component {

    override fun init(application: Application) {

//        Di.get<UsersRepository>().apply {
//            this.createUser(
//                login = "OlegAdmin",
//                password = "123",
//                profile = Profile(
//                    type = Profile.Type.ADMIN,
//                    name = "Oleg",
//                    surname = "Korelin",
//                    patronymic = "Sergeervich",
//                    avatar = null,
//                    about = null,
//                ),
//            ).launchIn(GlobalScope)
//        }

    }

}