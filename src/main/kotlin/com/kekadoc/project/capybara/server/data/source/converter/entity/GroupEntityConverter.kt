package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Group
import com.kekadoc.project.capybara.server.data.source.database.entity.GroupEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserGroupEntity

object GroupEntityConverter : Converter<GroupEntity, Group> {

    override fun convert(value: GroupEntity): Group = Group(
        id = value.id.value,
        name = value.name,
        members = value.members
            .also {
                println("GroupEntityConverter members ${it.toList()}")
            }
            .map(UserGroupEntity::user)
            .map(UserEntityConverter::convert),
    )

}