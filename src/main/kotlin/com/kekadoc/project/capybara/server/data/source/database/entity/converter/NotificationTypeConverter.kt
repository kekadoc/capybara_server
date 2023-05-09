package com.kekadoc.project.capybara.server.data.source.database.entity.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.domain.model.Message

object NotificationTypeConverter : Converter.Bidirectional<Message.Type, String> {

    override fun convert(value: Message.Type): String {
        return value.name
    }

    override fun revert(value: String): Message.Type {
        return runCatching {
            Message.Type.valueOf(value)
        }.getOrNull() ?: Message.Type.DEFAULT
    }

}