package com.kekadoc.project.capybara.server.data.repository.contacts

import com.kekadoc.project.capybara.server.data.model.Communications
import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.coroutines.flow.Flow

interface PublicContactsRepository {

    fun getContacts(): Flow<List<Contact>>

    fun getContact(contactId: Identifier): Flow<Contact>

    fun createContact(
        userContactId: Identifier,
        communications: Communications,
    ): Flow<Contact>

    fun deleteContact(
        contactId: Identifier,
    ): Flow<Unit>
}