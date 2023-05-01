package com.kekadoc.project.capybara.server.data.source.converter.dto

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.source.factory.dto.ProfileDtoFactory
import com.kekadoc.project.capybara.server.data.source.network.model.ContactDto

object ContactDtoConverter : Converter<Contact, ContactDto> {

    override fun convert(value: Contact): ContactDto = ContactDto(
        id = value.id,
        type = value.type.name,
        profile = ProfileDtoFactory.create(value.user),
        communications = value.user.communications.values
            .associate { (type, value) -> type.name to value },
    )

}