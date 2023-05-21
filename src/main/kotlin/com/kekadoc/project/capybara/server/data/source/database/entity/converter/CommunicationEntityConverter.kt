package com.kekadoc.project.capybara.server.data.source.database.entity.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.source.database.entity.CommunicationEntity
import com.kekadoc.project.capybara.server.domain.model.Communication

object CommunicationEntityConverter : Converter<CommunicationEntity, Communication> {

    override fun convert(value: CommunicationEntity): Communication = Communication(
        type = value.type,
        value = value.value,
        approved = value.approved,
    )

}