package com.kekadoc.project.capybara.server.utils.logging

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DefaultLogger : Logger {

    private val timeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm::ss.nn")

    private val logs = mutableMapOf<String, Logger.Log>()

    override fun log(tag: String): Logger.Log {
        return logs.getOrPut(tag) { LogImpl(tag) }
    }

    private class LogImpl(private val tag: String) : Logger.Log {

        private val time: String
            get() = timeFormatter.format(LocalDateTime.now())

        private val Throwable?.log: String?
            get() = if (this == null) null else "\n${this.stackTraceToString()}"

        override fun info(message: String?, throwable: Throwable?) {
            println("$time [$tag] INFO $message ${throwable.log}")
        }

        override fun debug(message: String?, throwable: Throwable?) {
            println("$time [$tag] DEBUG $message ${throwable.log}")
        }

        override fun warning(message: String?, throwable: Throwable?) {
            println("$time [$tag] WARNING $message ${throwable.log}")
        }

        override fun error(message: String?, throwable: Throwable?) {
            println("$time [$tag] ERROR $message ${throwable.log}")
        }

    }

}