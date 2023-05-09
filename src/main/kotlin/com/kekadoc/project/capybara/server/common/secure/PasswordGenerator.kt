package com.kekadoc.project.capybara.server.common.secure

import java.util.*

object PasswordGenerator {

    private val test = true

    fun generate(): String = if (test) "Test_Password" else UUID.randomUUID().toString().take(10)

}