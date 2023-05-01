package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.access.UserAccessToUser
import com.kekadoc.project.capybara.server.data.source.database.entity.UserAccessToUserEntity

object UserAccessToUserEntityConverter : Converter<UserAccessToUserEntity, UserAccessToUser> {

    override fun convert(value: UserAccessToUserEntity): UserAccessToUser = UserAccessToUser(
        fromUserId = value.fromUser.id.value,
        toUserId = value.toUser.id.value,
        readProfile = value.readProfile,
        sentNotification = value.sentNotification,
        contactInfo = value.contactInfo,
    )

}