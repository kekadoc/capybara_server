package com.kekadoc.project.capybara.server.data.source.converter.dto

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Group
import com.kekadoc.project.capybara.server.data.source.network.model.GroupDto

object GroupDtoConverter : Converter.Bidirectional<Group, GroupDto> {

    override fun convert(source: GroupDto): Group {
        throw NotImplementedError()
    }

    override fun revert(target: Group): GroupDto {
        return GroupDto(
            id = target.id,
            name = target.name,
            members = target.members.map { it.profile }.map { ProfileDtoConverter.revert(it) },
        )
    }

}