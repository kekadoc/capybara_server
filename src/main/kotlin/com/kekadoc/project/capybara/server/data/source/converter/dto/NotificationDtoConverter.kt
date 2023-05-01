package com.kekadoc.project.capybara.server.data.source.converter.dto

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.source.factory.dto.ProfileDtoFactory
import com.kekadoc.project.capybara.server.data.source.network.model.NotificationDto

object NotificationDtoConverter : Converter<Notification, NotificationDto> {

    override fun convert(value: Notification): NotificationDto = NotificationDto(
        id = value.id,
        authorId = value.authorId,
        type = TypeConverter.convert(value.type),
        content = ContentConverter.convert(value.content),
    )

    object ContentConverter : Converter.Bidirectional<Notification.Content, NotificationDto.Content> {

        override fun convert(
            value: Notification.Content,
        ): NotificationDto.Content = NotificationDto.Content(
            title = value.title,
            text = value.text,
            image = value.image,
        )

        override fun revert(
            value: NotificationDto.Content,
        ): Notification.Content = Notification.Content(
            title = value.title,
            text = value.text,
            image = value.image,
        )

    }

    object TypeConverter : Converter.Bidirectional<Notification.Type, NotificationDto.Type> {
        override fun convert(value: Notification.Type): NotificationDto.Type = when (value) {
            Notification.Type.FOR_GROUP -> NotificationDto.Type.FOR_GROUP
            Notification.Type.FOR_USER -> NotificationDto.Type.FOR_USER
            Notification.Type.DEFAULT -> NotificationDto.Type.DEFAULT
        }

        override fun revert(value: NotificationDto.Type): Notification.Type = when (value) {
            NotificationDto.Type.FOR_GROUP -> Notification.Type.FOR_GROUP
            NotificationDto.Type.FOR_USER -> Notification.Type.FOR_USER
            NotificationDto.Type.DEFAULT -> Notification.Type.DEFAULT
        }

    }

}