package com.kekadoc.project.capybara.server.data.source.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.source.network.model.ContactDto

object ContactDtoConverter : Converter<ContactDto, Contact> {

    override fun convert(source: Contact): ContactDto {
        return ContactDto(
            id = source.id,
            profile = source.user.profile.let { ProfileDtoConverter.revert(it) },
            communications = source.communications.let { CommunicationsDtoConverter.convert(it) },
        )
    }

}