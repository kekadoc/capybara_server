package com.kekadoc.project.capybara.server.data.source.converter.dto

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Communication
import com.kekadoc.project.capybara.server.data.source.network.model.CommunicationDto

object CommunicationDtoConverter : Converter.Bidirectional<CommunicationDto, Communication> {

    override fun convert(source: Communication): CommunicationDto = CommunicationDto(
        type = TypeConverter.convert(source.type),
        value = source.value,
    )

    override fun revert(target: CommunicationDto): Communication = Communication(
        type = TypeConverter.revert(target.type),
        value = target.value,
    )

    object TypeConverter : Converter.Bidirectional<CommunicationDto.Type, Communication.Type> {

        override fun convert(source: Communication.Type): CommunicationDto.Type = when (source) {
            Communication.Type.PHONE -> CommunicationDto.Type.PHONE
            Communication.Type.EMAIL -> CommunicationDto.Type.EMAIL
            Communication.Type.VIBER -> CommunicationDto.Type.VIBER
            Communication.Type.WHATS_APP -> CommunicationDto.Type.WHATS_APP
            Communication.Type.TELEGRAM -> CommunicationDto.Type.TELEGRAM
            Communication.Type.UNKNOWN -> CommunicationDto.Type.UNKNOWN
        }

        override fun revert(target: CommunicationDto.Type): Communication.Type = when (target) {
            CommunicationDto.Type.PHONE -> Communication.Type.PHONE
            CommunicationDto.Type.EMAIL -> Communication.Type.EMAIL
            CommunicationDto.Type.VIBER -> Communication.Type.VIBER
            CommunicationDto.Type.WHATS_APP -> Communication.Type.WHATS_APP
            CommunicationDto.Type.TELEGRAM -> Communication.Type.TELEGRAM
            CommunicationDto.Type.UNKNOWN -> Communication.Type.UNKNOWN
        }

    }

}