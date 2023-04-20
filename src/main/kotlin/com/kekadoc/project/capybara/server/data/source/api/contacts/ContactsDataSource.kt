package com.kekadoc.project.capybara.server.data.source.api.contacts

import com.kekadoc.project.capybara.server.data.model.CommunicationType
import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.coroutines.flow.Flow

interface ContactsDataSource {

    fun getContacts(ids: List<Identifier>): Flow<List<Contact>>

    fun getContact(contactId: Identifier): Flow<Contact>

    fun createContact(
        userContactId: Identifier,
        communications: Map<CommunicationType, String>,
    ): Flow<Contact>

    fun updateContact(
        contactId: Identifier,
        communications: Map<CommunicationType, String>,
    ): Flow<Contact>

    fun deleteContact(
        contactId: Identifier,
    ): Flow<Unit>

}