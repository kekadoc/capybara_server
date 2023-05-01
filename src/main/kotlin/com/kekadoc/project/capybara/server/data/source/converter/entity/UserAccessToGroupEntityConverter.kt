package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.access.UserAccessToGroup
import com.kekadoc.project.capybara.server.data.model.access.UserAccessToUser
import com.kekadoc.project.capybara.server.data.source.database.entity.UserAccessToGroupEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserAccessToUserEntity

object UserAccessToGroupEntityConverter : Converter<UserAccessToGroupEntity, UserAccessToGroup> {

    override fun convert(value: UserAccessToGroupEntity): UserAccessToGroup = UserAccessToGroup(
        userId = value.user.id.value,
        groupId = value.group.id.value,
        readInfo = value.readInfo,
        readMembers = value.readMembers,
        sentNotification = value.sentNotification,
    )

}