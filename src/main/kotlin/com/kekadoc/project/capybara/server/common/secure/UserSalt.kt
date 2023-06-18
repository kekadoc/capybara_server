package com.kekadoc.project.capybara.server.common.secure

import com.kekadoc.project.capybara.server.Config
import com.kekadoc.project.capybara.server.domain.model.Identifier

object UserSalt {

    fun get(id: Identifier, login: String): String = buildString {
        append(Hash.hash(Config.userSalt, "one"))
        append(Hash.hash(id.toString().reversed(), "two"))
        append(Hash.hash(login, "three"))
    }

}