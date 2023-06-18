package com.kekadoc.project.capybara.server.common.time

import com.kekadoc.project.capybara.server.common.component.Component
import io.ktor.server.application.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Time : Component {

    private val moscowTimeZone = ZoneId.of("Europe/Moscow")

    private var locale: Locale? = Locale.ENGLISH

    private val serverDateFormat = DateTimeFormatter.ISO_ZONED_DATE_TIME

    private val simpleUIFormat by lazy {
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", locale ?: Locale.ENGLISH)
    }

    fun formatToServer(time: LocalDateTime): String {
        return serverDateFormat.format(time.atZone(moscowTimeZone))
    }

    fun timeFromServer(time: String): ZonedDateTime {
        return ZonedDateTime.parse(time, serverDateFormat)
    }

    fun uiFormat(time: ZonedDateTime): String {
        return simpleUIFormat.format(time)
    }

    override suspend fun init(application: Application) {

    }

}