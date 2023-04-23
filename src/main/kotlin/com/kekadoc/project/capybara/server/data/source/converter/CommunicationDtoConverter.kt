package com.kekadoc.project.capybara.server.data.source.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Communication
import com.kekadoc.project.capybara.server.data.source.network.model.CommunicationDto

object CommunicationDtoConverter : Converter<CommunicationDto, Communication> {

    override fun convert(source: Communication): CommunicationDto {
        return CommunicationDto(
            name = source.name,
            value = source.value,
        )
    }

}