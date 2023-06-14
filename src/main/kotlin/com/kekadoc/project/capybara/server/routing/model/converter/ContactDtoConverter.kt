package com.kekadoc.project.capybara.server.routing.model.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.domain.model.Contact
import com.kekadoc.project.capybara.server.routing.model.factory.ProfileDtoFactory
import com.kekadoc.project.capybara.server.routing.model.contact.ContactDto

object ContactDtoConverter : Converter<Contact, ContactDto> {

    override fun convert(value: Contact): ContactDto = ContactDto(
        id = value.id,
        type = value.type.name,
        profile = ProfileDtoFactory.create(value.user),
        communications = value.user.communications.values
            .associate { (type, value) -> type.name to value },
    )

}