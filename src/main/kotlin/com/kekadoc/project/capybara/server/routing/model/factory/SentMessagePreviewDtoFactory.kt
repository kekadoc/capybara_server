package com.kekadoc.project.capybara.server.routing.model.factory

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.common.time.Time
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.message.MessageInfo
import com.kekadoc.project.capybara.server.routing.model.converter.MessageActionDtoConverter
import com.kekadoc.project.capybara.server.routing.model.converter.MessageStatusDtoConverter
import com.kekadoc.project.capybara.server.routing.model.converter.MessageTypeDtoConverter
import com.kekadoc.project.capybara.server.routing.model.message.SentMessagePreviewDto

object SentMessagePreviewDtoFactory : Factory.Twin<SentMessagePreviewDto, Message, MessageInfo> {

    override fun invoke(
        message: Message,
        messageInfo: MessageInfo,
    ): SentMessagePreviewDto = SentMessagePreviewDto(
        id = message.id,
        type = MessageTypeDtoConverter.convert(message.type),
        title = message.title,
        text = message.text,
        date = Time.formatToServer(message.date),
        actions = message.actions.map(MessageActionDtoConverter::convert),
        isMultiAnswer = message.isMultiAnswer,
        addresseeGroupIds = messageInfo.addresseeGroups.map(MessageInfo.GroupInfo::groupId),
        addresseeUsersIds = messageInfo.addresseeUsers.map(MessageInfo.FromUserInfo::userId),
        status = MessageStatusDtoConverter.revert(messageInfo.status),
    )

}