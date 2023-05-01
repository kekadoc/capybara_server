package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Communication
import com.kekadoc.project.capybara.server.data.source.database.entity.CommunicationEntity

object CommunicationEntityConverter : Converter<CommunicationEntity, Communication> {

    override fun convert(value: CommunicationEntity): Communication = Communication(
        type = value.type,
        value = value.value,
    )

}