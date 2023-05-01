package com.kekadoc.project.capybara.server.data.source.api.contacts

import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.model.Identifier

interface PublicContactsDataSource {

    suspend fun getContacts(): List<Contact>

    suspend fun getContact(contactId: Identifier): Contact?

    suspend fun addContact(userId: Identifier): Contact

    suspend fun deleteContact(contactId: Identifier): Contact?

}