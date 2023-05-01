package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Notification

object NotificationTypeConverter : Converter.Bidirectional<Notification.Type, String> {

    override fun convert(value: Notification.Type): String {
        return value.name
    }

    override fun revert(value: String): Notification.Type {
        return runCatching {
            Notification.Type.valueOf(value)
        }.getOrNull() ?: Notification.Type.DEFAULT
    }

}