package com.kekadoc.project.capybara.server.data.source.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Communication
import com.kekadoc.project.capybara.server.data.model.Communications
import com.kekadoc.project.capybara.server.data.source.network.model.CommunicationsDto

object CommunicationsDtoConverter : Converter.Bidirectional<CommunicationsDto, Communications> {

    override fun convert(source: Communications): CommunicationsDto {
        return CommunicationsDto(
            values = source.values.map { CommunicationDtoConverter.convert(it) }
        )
    }

    override fun revert(target: CommunicationsDto): Communications {
        return Communications(
            values = target.values.map { Communication(it.name, it.value) }
        )
    }

}