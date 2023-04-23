package com.kekadoc.project.capybara.server.common.tools

import java.util.*

object Salt {

    fun generate(): String {
        return UUID.randomUUID().toString().take(10)
    }

}