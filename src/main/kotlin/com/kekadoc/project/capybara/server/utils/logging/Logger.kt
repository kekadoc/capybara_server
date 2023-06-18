package com.kekadoc.project.capybara.server.utils.logging

interface Logger {

    companion object : Logger by DefaultLogger {

        private const val DEFAULT_TAG = "SERVER"

    }

    fun log(tag: String = DEFAULT_TAG): Log

    interface Log {

        fun info(message: String? = null, throwable: Throwable? = null)

        fun debug(message: String? = null, throwable: Throwable? = null)

        fun warning(message: String? = null, throwable: Throwable? = null)

        fun error(message: String? = null, throwable: Throwable? = null)

    }

}