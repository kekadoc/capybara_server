package com.kekadoc.project.capybara.server.data.repository.contacts

import com.kekadoc.project.capybara.server.data.model.Communications
import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.source.api.contacts.ContactsDataSource
import kotlinx.coroutines.flow.Flow

class PublicContactsRepositoryImpl(
    private val contactsDataSource: ContactsDataSource,
) : PublicContactsRepository {

    override fun getContacts(): Flow<List<Contact>> {
        return contactsDataSource.getContacts()
    }

    override fun getContact(contactId: Identifier): Flow<Contact> {
        return contactsDataSource.getContact(contactId)
    }

    override fun createContact(
        userContactId: Identifier,
        communications: Communications,
    ): Flow<Contact> {
        return contactsDataSource.createContact(userContactId, communications)
    }

    override fun deleteContact(contactId: Identifier): Flow<Unit> {
        return contactsDataSource.deleteContact(contactId)
    }

}