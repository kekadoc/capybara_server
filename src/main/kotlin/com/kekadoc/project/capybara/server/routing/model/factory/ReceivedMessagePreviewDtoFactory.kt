package com.kekadoc.project.capybara.server.routing.model.factory

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.common.time.Time
import com.kekadoc.project.capybara.server.domain.model.User
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.message.MessageForUser
import com.kekadoc.project.capybara.server.routing.model.converter.MessageActionDtoConverter
import com.kekadoc.project.capybara.server.routing.model.converter.MessageTypeDtoConverter
import com.kekadoc.project.capybara.server.routing.model.message.ReceivedMessagePreviewDto

object ReceivedMessagePreviewDtoFactory :
    Factory.Triple<ReceivedMessagePreviewDto, Message, MessageForUser, User> {

    override fun invoke(
        message: Message,
        messageForUser: MessageForUser,
        author: User,
    ): ReceivedMessagePreviewDto = ReceivedMessagePreviewDto(
        id = message.id,
        author = ProfileDtoFactory.create(author),
        type = MessageTypeDtoConverter.convert(message.type),
        title = message.title,
        message = message.text,
        date = Time.formatToServer(message.date),
        read = messageForUser.read,
        answerIds = messageForUser.answerIds,
        actions = message.actions.map(MessageActionDtoConverter::convert),
        isMultiAnswer = message.isMultiAnswer,
    )

}