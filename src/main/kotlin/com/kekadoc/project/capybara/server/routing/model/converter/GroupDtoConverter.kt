package com.kekadoc.project.capybara.server.routing.model.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.routing.model.group.GroupDto
import com.kekadoc.project.capybara.server.domain.model.group.Group

object GroupDtoConverter : Converter<Group, GroupDto> {

    override fun convert(value: Group): GroupDto = GroupDto(
        id = value.id,
        name = value.name,
        membersIds = value.members.map { it.id },
    )

}