package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.source.database.entity.ContactEntity

object ContactEntityConverter : Converter<Contact, ContactEntity> {

    override fun convert(source: ContactEntity): Contact = Contact(
        id = source.id.toString(),
        user = UserEntityConverter.convert(source.user),
    )

}