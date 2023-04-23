package com.kekadoc.project.capybara.server.intercator.contacts

import com.kekadoc.project.capybara.server.routing.api.contacts.model.*

interface ContactsInteractor {

    suspend fun getContacts(
        authToken: String,
    ): GetAllContactsResponse

    suspend fun createContact(
        authToken: String,
        request: CreateContactRequest,
    ): CreateContactResponse

    suspend fun getContact(
        authToken: String,
        contactId: String,
    ): GetContactResponse

    suspend fun deleteContact(
        authToken: String,
        contactId: String,
    )

}