package com.kekadoc.project.capybara.server.data.source.database.entity.factory

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.common.time.Time
import com.kekadoc.project.capybara.server.data.source.database.entity.MessageEntity
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.message.MessageAction
import com.kekadoc.project.capybara.server.domain.model.message.MessageNotifications
import com.kekadoc.project.capybara.server.domain.model.message.MessageType

object MessageFactory : Factory.Single<MessageEntity, Message> {

    override fun create(value: MessageEntity): Message = Message(
        id = value.id.value,
        authorId = value.author.id.value,
        type = MessageType.valueOf(value.type),
        title = value.contentTitle,
        text = value.contentText,
        date = Time.timeFromServer(value.date),
        addresseeUserIds = value.addresseeUsers.map { entity -> entity.userId.id.value },
        addresseeGroupIds = value.addresseeGroups.map { entity -> entity.group.id.value },
        notifications = MessageNotifications(
            email = value.notificationEmail,
            sms = value.notificationSms,
            app = value.notificationApp,
            messengers = value.notificationMessengers,
        ),
        actions = value.actions
            ?.mapIndexed { index, text ->
                MessageAction(
                    id = index.toLong(),
                    text = text,
                )
            }
            .orEmpty(),
        isMultiAnswer = value.isMultiAnswer,
    )

}