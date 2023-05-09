package com.kekadoc.project.capybara.server.data.source.database.entity.factory

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.data.source.database.entity.MessageEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.converter.NotificationTypeConverter
import com.kekadoc.project.capybara.server.domain.model.Message

object MessageFactory : Factory.Single<MessageEntity, Message> {

    override fun create(value: MessageEntity): Message = Message(
        id = value.id.value,
        authorId = value.author.id.value,
        type = NotificationTypeConverter.revert(value.type),
        content = Message.Content(
            title = value.contentTitle,
            text = value.contentText,
            image = value.contentImage,
        ),
        addresseeUserIds = value.addresseeUsers.map { entity -> entity.userId.id.value },
        addresseeGroupIds = value.addresseeGroups.map { entity -> entity.group.id.value },
        notifications = Message.Notifications(
            email = value.notificationEmail,
            sms = value.notificationSms,
            app = value.notificationApp,
            messengers = value.notificationMessengers,
        ),
        actions = Message.Actions(
            action1 = value.action1,
            action2 = value.action2,
            action3 = value.action3,
        )
    )

}