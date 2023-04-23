package com.kekadoc.project.capybara.server

import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.source.api.user.UsersDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

fun main() {
    Application.init()
    test()
}

private fun test() {

    CoroutineScope(Dispatchers.IO).launch {
        val usersDataSource = UsersDataSourceImpl()
        val user = usersDataSource.createUser(
            login = "Login123",
            profile = Profile(
                type = Profile.Type.USER,
                name = "Oleg",
                surname = "Korelin",
                patronymic = "Sergeevich",
                avatar = "",
                role = "",
                about = "",
            ),
        ).firstOrNull()
        val findUser = usersDataSource.getUserById(id = user!!.id).single()
        println("findUser $findUser")
    }
}