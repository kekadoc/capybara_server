package com.kekadoc.project.capybara.server.data.source.database.entity.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.source.database.entity.UserAccessToGroupEntity
import com.kekadoc.project.capybara.server.domain.model.UserAccessToGroup

object UserAccessToGroupEntityConverter : Converter<UserAccessToGroupEntity, UserAccessToGroup> {

    override fun convert(value: UserAccessToGroupEntity): UserAccessToGroup = UserAccessToGroup(
        userId = value.user.id.value,
        groupId = value.group.id.value,
        readInfo = value.readInfo,
        readMembers = value.readMembers,
        sentNotification = value.sentNotification,
    )

}