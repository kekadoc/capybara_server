package com.kekadoc.project.capybara.server.data.source.network.model.factory

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.data.source.network.model.MessageInfoDto
import com.kekadoc.project.capybara.server.data.source.network.model.converter.MessageDtoConverter
import com.kekadoc.project.capybara.server.domain.model.MessageInfo

object MessageInfoDtoFactory : Factory.Single<MessageInfo, MessageInfoDto> {

    override fun create(value: MessageInfo): MessageInfoDto {
        return MessageInfoDto(
            message = MessageDtoConverter.convert(value = value.message),
            addresseeGroups = value.addresseeGroups.map { groupInfo ->
                MessageInfoDto.GroupInfo(
                    id = groupInfo.id,
                    name = groupInfo.name,
                    members = groupInfo.members.map { userInfo ->
                        MessageInfoDto.FromUserInfo(
                            userId = userInfo.userId,
                            received = userInfo.received,
                            read = userInfo.read,
                            answer = userInfo.answer,
                        )
                    }
                )
            },
            addresseeUsers = value.addresseeUsers.map { userInfo ->
                MessageInfoDto.FromUserInfo(
                    userId = userInfo.userId,
                    received = userInfo.received,
                    read = userInfo.read,
                    answer = userInfo.answer,
                )
            },
            status = when (value.status) {
                MessageInfo.Status.UNDEFINED -> MessageInfoDto.Status.UNDEFINED
                MessageInfo.Status.RECEIVED -> MessageInfoDto.Status.RECEIVED
                MessageInfo.Status.SENT -> MessageInfoDto.Status.SENT
                MessageInfo.Status.CANCELED -> MessageInfoDto.Status.CANCELED
                MessageInfo.Status.REJECTED -> MessageInfoDto.Status.REJECTED
                MessageInfo.Status.FAILED -> MessageInfoDto.Status.FAILED
            },
        )
    }
}