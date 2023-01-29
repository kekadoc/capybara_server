package com.kekadoc.project.capybara.server

import com.kekadoc.project.capybara.model.ScheduleSource

internal object Config {

    const val VERSION = "1.2.0"
    const val PORT_KEY = "PORT"
    const val DEFAULT_PORT_CODE = 8080
    const val API_KEY_PARAMETER = "apiKey"
    
    fun isSourceAvailable(source: ScheduleSource): Boolean {
        return when(source) {
            ScheduleSource.CHF_PNIPU_EXTRAMURAL -> false
            else -> true
        }
    }
}