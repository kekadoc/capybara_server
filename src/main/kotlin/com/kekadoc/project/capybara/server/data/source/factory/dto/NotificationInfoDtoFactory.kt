package com.kekadoc.project.capybara.server.data.source.factory.dto

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.data.model.NotificationInfo
import com.kekadoc.project.capybara.server.data.source.converter.dto.NotificationDtoConverter
import com.kekadoc.project.capybara.server.data.source.network.model.NotificationInfoDto

object NotificationInfoDtoFactory : Factory.Single<NotificationInfo, NotificationInfoDto> {

    override fun create(value: NotificationInfo): NotificationInfoDto {
        return NotificationInfoDto(
            notification = NotificationDtoConverter.convert(value = value.notification),
            addresseeGroups = value.addresseeGroups.map { groupInfo ->
                NotificationInfoDto.GroupInfo(
                    id = groupInfo.id,
                    name = groupInfo.name,
                    members = groupInfo.members.map { userInfo ->
                        NotificationInfoDto.FromUserInfo(
                            userId = userInfo.userId,
                            received = userInfo.received,
                            read = userInfo.read,
                            answer = userInfo.answer,
                        )
                    }
                )
            },
            addresseeUsers = value.addresseeUsers.map { userInfo ->
                NotificationInfoDto.FromUserInfo(
                    userId = userInfo.userId,
                    received = userInfo.received,
                    read = userInfo.read,
                    answer = userInfo.answer,
                )
            },
            status = when (value.status) {
                NotificationInfo.Status.UNDEFINED -> NotificationInfoDto.Status.UNDEFINED
                NotificationInfo.Status.RECEIVED -> NotificationInfoDto.Status.RECEIVED
                NotificationInfo.Status.SENT -> NotificationInfoDto.Status.SENT
                NotificationInfo.Status.CANCELED -> NotificationInfoDto.Status.CANCELED
                NotificationInfo.Status.REJECTED -> NotificationInfoDto.Status.REJECTED
                NotificationInfo.Status.FAILED -> NotificationInfoDto.Status.FAILED
            },
        )
    }
}