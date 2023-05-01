package com.kekadoc.project.capybara.server.secure

import java.util.UUID

object PasswordGenerator {

    private val test = true

    fun generate(): String = if (test) "Test_Password" else UUID.randomUUID().toString().take(10)

}