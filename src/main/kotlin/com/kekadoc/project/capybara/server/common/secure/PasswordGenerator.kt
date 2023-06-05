package com.kekadoc.project.capybara.server.common.secure

import java.util.*

object PasswordGenerator {

    private val test = true

    fun generate(): String = if (test) "123" else UUID.randomUUID().toString().take(10)

}