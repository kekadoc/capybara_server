package com.kekadoc.project.capybara.server.data.source.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.source.network.model.NotificationDto

object NotificationDtoConverter : Converter<NotificationDto, Notification> {

    override fun convert(source: Notification): NotificationDto {
        return NotificationDto(
            id = source.id,
            author = source.author.profile.let(ProfileDtoConverter::revert),
            type = TypeConverter.convert(source.type),
            content = ContentConverter.convert(source.content),
        )
    }

    object ContentConverter : Converter.Bidirectional<NotificationDto.Content, Notification.Content> {

        override fun convert(source: Notification.Content): NotificationDto.Content {
            return NotificationDto.Content(
                title = source.title,
                text = source.text,
                image = source.image,
            )
        }

        override fun revert(target: NotificationDto.Content): Notification.Content {
            return Notification.Content(
                title = target.title,
                text = target.text,
                image = target.image,
            )
        }

    }

    object TypeConverter : Converter.Bidirectional<NotificationDto.Type, Notification.Type> {
        override fun convert(source: Notification.Type): NotificationDto.Type {
            return when (source) {
                Notification.Type.FOR_GROUP -> NotificationDto.Type.FOR_GROUP
                Notification.Type.FOR_USER -> NotificationDto.Type.FOR_USER
            }
        }

        override fun revert(target: NotificationDto.Type): Notification.Type {
            return when (target) {
                NotificationDto.Type.FOR_GROUP -> Notification.Type.FOR_GROUP
                NotificationDto.Type.FOR_USER -> Notification.Type.FOR_USER
            }
        }

    }

}