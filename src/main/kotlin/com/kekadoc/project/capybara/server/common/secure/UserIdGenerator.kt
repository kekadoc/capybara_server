package com.kekadoc.project.capybara.server.common.secure

import java.util.UUID

object UserIdGenerator {

    fun generate(): String = UUID.randomUUID().toString()

}