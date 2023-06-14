package com.kekadoc.project.capybara.server.data.source.database.entity.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.common.converter.convert
import com.kekadoc.project.capybara.server.data.source.database.entity.PublicContactEntity
import com.kekadoc.project.capybara.server.domain.model.user.Contact

object PublicContactEntityConverter : Converter<PublicContactEntity, Contact> {

    override fun convert(value: PublicContactEntity): Contact = Contact(
        id = value.id.value,
        type = Contact.Type.PUBLIC,
        user = value.user.convert(UserEntityConverter)
    )

}