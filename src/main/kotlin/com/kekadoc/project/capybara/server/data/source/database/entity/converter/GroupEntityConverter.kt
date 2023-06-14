package com.kekadoc.project.capybara.server.data.source.database.entity.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.source.database.entity.GroupEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserGroupEntity
import com.kekadoc.project.capybara.server.domain.model.group.Group

object GroupEntityConverter : Converter<GroupEntity, Group> {

    override fun convert(value: GroupEntity): Group = Group(
        id = value.id.value,
        name = value.name,
        members = value.members
            .map(UserGroupEntity::user)
            .map(UserEntityConverter::convert),
    )

}