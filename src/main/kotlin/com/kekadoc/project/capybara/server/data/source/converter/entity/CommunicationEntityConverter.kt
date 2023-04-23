package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Communication
import com.kekadoc.project.capybara.server.data.source.database.entity.CommunicationEntity

object CommunicationEntityConverter : Converter<Communication, CommunicationEntity> {

    override fun convert(source: CommunicationEntity): Communication = Communication(
        type = source.type,
        value = source.value,
    )

}