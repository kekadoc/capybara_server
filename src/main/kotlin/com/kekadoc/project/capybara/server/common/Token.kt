package com.kekadoc.project.capybara.server.common

import java.util.*

object Token {
    
    fun generate(): String {
        return UUID.randomUUID().toString()
    }
}