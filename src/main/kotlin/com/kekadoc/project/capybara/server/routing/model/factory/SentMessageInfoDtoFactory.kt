package com.kekadoc.project.capybara.server.routing.model.factory

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.common.time.Time
import com.kekadoc.project.capybara.server.domain.model.message.MessageInfo
import com.kekadoc.project.capybara.server.routing.model.converter.MessageActionDtoConverter
import com.kekadoc.project.capybara.server.routing.model.converter.NotificationsConverter
import com.kekadoc.project.capybara.server.routing.model.message.MessageStatusDto
import com.kekadoc.project.capybara.server.routing.model.message.MessageTypeDto
import com.kekadoc.project.capybara.server.routing.model.message.SentMessageInfoDto

object SentMessageInfoDtoFactory : Factory.Single<MessageInfo, SentMessageInfoDto> {

    override fun create(value: MessageInfo): SentMessageInfoDto {
        return SentMessageInfoDto(
            id = value.message.id,
            type = MessageTypeDto.valueOf(value.message.type.name),
            title = value.message.title,
            text = value.message.text,
            date = Time.formatToServer(value.message.date),
            actions = value.message.actions.map(MessageActionDtoConverter::convert),
            isMultiAnswer = value.message.isMultiAnswer,
            addresseeGroups = value.addresseeGroups.map(::createGroupInfo),
            addresseeUsers = value.addresseeUsers.map(::createFromUserInfo),
            status = MessageStatusDto.valueOf(value.status.name),
            notifications = NotificationsConverter.convert(value.message.notifications)
        )
    }

    private fun createGroupInfo(value: MessageInfo.GroupInfo): SentMessageInfoDto.GroupInfo {
        return SentMessageInfoDto.GroupInfo(
            groupId = value.groupId,
            name = value.name,
            members = value.members.map(::createFromUserInfo),
        )
    }

    private fun createFromUserInfo(value: MessageInfo.FromUserInfo): SentMessageInfoDto.FromUserInfo {
        return SentMessageInfoDto.FromUserInfo(
            userId = value.userId,
            received = value.received,
            read = value.read,
            answer = value.answerIds,
        )
    }

}