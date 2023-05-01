package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.common.converter.convert
import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.source.database.entity.PublicContactEntity

object PublicContactEntityConverter : Converter<PublicContactEntity, Contact> {

    override fun convert(value: PublicContactEntity): Contact = Contact(
        id = value.id.value,
        type = Contact.Type.PUBLIC,
        user = value.user.convert(UserEntityConverter)
    )

}