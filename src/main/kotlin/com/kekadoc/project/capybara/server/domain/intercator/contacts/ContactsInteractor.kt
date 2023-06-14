package com.kekadoc.project.capybara.server.domain.intercator.contacts

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.contacts.model.*

interface ContactsInteractor {

    suspend fun getAllContacts(
        authToken: Token,
    ): GetAllContactsResponse

    suspend fun getContact(
        authToken: Token,
        contactId: Identifier,
    ): GetContactResponseDto

    suspend fun getAllPublicContacts(
        authToken: Token,
    ): GetAllPublicContactsResponseDto

    suspend fun getPublicContact(
        authToken: Token,
        contactId: Identifier,
    ): GetPublicContactResponseDto

    suspend fun addPublicContact(
        authToken: Token,
        request: AddPublicContactRequestDto,
    ): AddPublicContactResponseDto

    suspend fun deletePublicContact(
        authToken: Token,
        contactId: Identifier,
    ): DeletePublicContactResponseDto

}