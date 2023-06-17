package com.kekadoc.project.capybara.server.common.secure

import com.kekadoc.project.capybara.server.Config
import java.util.*

object PasswordGenerator {

    fun generate(): String = if (Config.isDebugDefaultSimplePassword) {
        "1234"
    } else {
        UUID.randomUUID().toString().take(10)
    }

}