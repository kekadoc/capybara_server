package com.kekadoc.project.capybara.server.data.source.factory.dto

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.source.converter.entity.NotificationTypeConverter
import com.kekadoc.project.capybara.server.data.source.database.entity.NotificationEntity

object NotificationFactory : Factory.Single<NotificationEntity, Notification> {

    override fun create(value: NotificationEntity): Notification = Notification(
        id = value.id.value,
        authorId = value.author.id.value,
        type = NotificationTypeConverter.revert(value.type),
        content = Notification.Content(
            title = value.contentTitle,
            text = value.contentText,
            image = value.contentImage,
        ),
        addresseeUserIds = value.addresseeUsers.map { it.userId.id.value },
        addresseeGroupIds = value.addresseeGroups.map { it.group.id.value },
    )

}