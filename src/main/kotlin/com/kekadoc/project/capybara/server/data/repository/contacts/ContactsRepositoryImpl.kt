package com.kekadoc.project.capybara.server.data.repository.contacts

import com.kekadoc.project.capybara.server.data.model.Communications
import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.source.api.contacts.ContactsDataSource
import kotlinx.coroutines.flow.Flow

class ContactsRepositoryImpl(
    private val contactsDataSource: ContactsDataSource,
) : ContactsRepository {

    override fun getContacts(ids: List<Identifier>): Flow<List<Contact>> {
        return contactsDataSource.getContacts(ids)
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

    override fun updateContact(
        contactId: Identifier,
        communications: Communications,
    ): Flow<Contact> {
        return contactsDataSource.updateContact(contactId, communications)
    }

    override fun deleteContact(contactId: Identifier): Flow<Unit> {
        return contactsDataSource.deleteContact(contactId)
    }

}