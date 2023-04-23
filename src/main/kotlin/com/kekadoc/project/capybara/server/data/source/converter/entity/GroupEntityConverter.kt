package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Group
import com.kekadoc.project.capybara.server.data.source.database.entity.GroupEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserGroupEntity

object GroupEntityConverter : Converter<Group, GroupEntity> {

    override fun convert(source: GroupEntity): Group = Group(
        id = source.id.value.toString(),
        name = source.name,
        members = source.members
            .also {
                println("GroupEntityConverter members ${it.toList()}")
            }
            .map(UserGroupEntity::user)
            .map(UserEntityConverter::convert),
    )

}