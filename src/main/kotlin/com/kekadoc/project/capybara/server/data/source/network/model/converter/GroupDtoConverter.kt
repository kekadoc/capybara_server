package com.kekadoc.project.capybara.server.data.source.network.model.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.domain.model.Group
import com.kekadoc.project.capybara.server.data.source.network.model.GroupDto
import java.util.*

object GroupDtoConverter : Converter<Group, GroupDto> {

    override fun convert(value: Group): GroupDto = GroupDto(
        id = UUID.fromString(value.id.toString()),
        name = value.name,
        membersIds = value.members.map { it.id },
    )

}