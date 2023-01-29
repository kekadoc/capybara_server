package com.kekadoc.project.capybara.server.common

object Id {
    
    fun generate(): String {
        return System.currentTimeMillis().toString()
    }
}