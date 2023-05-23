package com.kekadoc.project.capybara.server.common.time

import com.kekadoc.project.capybara.server.common.component.Component
import io.ktor.server.application.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Time : Component {

    private var locale: Locale? = Locale.ENGLISH

    private val serverDateFormat = DateTimeFormatter.ISO_ZONED_DATE_TIME

    private val simpleUIFormat by lazy {
        DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm", locale ?: Locale.ENGLISH)
    }

    fun formatToServer(time: ZonedDateTime): String {
        return serverDateFormat.format(time)
    }

    fun timeFromServer(time: String): ZonedDateTime {
        return ZonedDateTime.parse(time, serverDateFormat)
    }

    fun uiFormat(time: ZonedDateTime): String {
        return simpleUIFormat.format(time)
    }

    override fun init(application: Application) {

    }

}