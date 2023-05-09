package com.kekadoc.project.capybara.server.data.source.network.model.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.source.network.model.MessageDto
import com.kekadoc.project.capybara.server.domain.model.Message

object MessageDtoConverter : Converter<Message, MessageDto> {

    override fun convert(value: Message): MessageDto = MessageDto(
        id = value.id,
        authorId = value.authorId,
        type = TypeConverter.convert(value.type),
        content = ContentConverter.convert(value.content),
        notifications = NotificationsConverter.convert(value.notifications),
        actions = MessageDto.Actions(
            action1 = value.actions?.action1,
            action2 = value.actions?.action2,
            action3 = value.actions?.action3,
        )
    )

    object ActionsConverter : Converter.Bidirectional<Message.Actions, MessageDto.Actions> {

        override fun convert(
            value: Message.Actions,
        ): MessageDto.Actions = MessageDto.Actions(
            action1 = value.action1,
            action2 = value.action2,
            action3 = value.action3,
        )

        override fun revert(
            value: MessageDto.Actions,
        ): Message.Actions = Message.Actions(
            action1 = value.action1,
            action2 = value.action2,
            action3 = value.action3,
        )

    }

    object NotificationsConverter : Converter.Bidirectional<Message.Notifications, MessageDto.Notifications> {

        override fun convert(
            value: Message.Notifications,
        ): MessageDto.Notifications = MessageDto.Notifications(
            email = value.email,
            sms = value.sms,
            app = value.app,
            messengers = value.messengers,
        )

        override fun revert(
            value: MessageDto.Notifications,
        ): Message.Notifications = Message.Notifications(
            email = value.email,
            sms = value.sms,
            app = value.app,
            messengers = value.messengers,
        )

    }

    object ContentConverter : Converter.Bidirectional<Message.Content, MessageDto.Content> {

        override fun convert(
            value: Message.Content,
        ): MessageDto.Content = MessageDto.Content(
            title = value.title,
            text = value.text,
            image = value.image,
        )

        override fun revert(
            value: MessageDto.Content,
        ): Message.Content = Message.Content(
            title = value.title,
            text = value.text,
            image = value.image,
        )

    }

    object TypeConverter : Converter.Bidirectional<Message.Type, MessageDto.Type> {
        override fun convert(value: Message.Type): MessageDto.Type = when (value) {
            Message.Type.FOR_GROUP -> MessageDto.Type.FOR_GROUP
            Message.Type.FOR_USER -> MessageDto.Type.FOR_USER
            Message.Type.DEFAULT -> MessageDto.Type.DEFAULT
        }

        override fun revert(value: MessageDto.Type): Message.Type = when (value) {
            MessageDto.Type.FOR_GROUP -> Message.Type.FOR_GROUP
            MessageDto.Type.FOR_USER -> Message.Type.FOR_USER
            MessageDto.Type.DEFAULT -> Message.Type.DEFAULT
        }

    }

}