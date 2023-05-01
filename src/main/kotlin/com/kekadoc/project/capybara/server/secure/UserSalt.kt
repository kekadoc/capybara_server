package com.kekadoc.project.capybara.server.secure

import com.kekadoc.project.capybara.server.common.secure.Hash
import com.kekadoc.project.capybara.server.data.model.Identifier
import java.util.*

object UserSalt {

    private const val USERS_SALT = "eChgfjbmTtkmupRgdp8p2RST"

    fun get(id: Identifier, login: String): String {
        return Hash.hash(USERS_SALT, "one") + Hash.hash(id.toString().reversed(), "two") + Hash.hash(login, "three").also {
            println("user hash: $it")
        }
    }

}