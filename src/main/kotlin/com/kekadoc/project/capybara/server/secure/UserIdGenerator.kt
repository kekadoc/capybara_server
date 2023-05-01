package com.kekadoc.project.capybara.server.secure

import java.util.UUID

object UserIdGenerator {

    fun generate(): String = UUID.randomUUID().toString()

}