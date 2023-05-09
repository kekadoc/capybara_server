package com.kekadoc.project.capybara.server.data.repository.contacts

import com.kekadoc.project.capybara.server.common.extensions.flowOf
import com.kekadoc.project.capybara.server.data.source.api.contacts.PublicContactsDataSource
import com.kekadoc.project.capybara.server.domain.model.Contact
import com.kekadoc.project.capybara.server.domain.model.Identifier
import kotlinx.coroutines.flow.Flow

class PublicContactsRepositoryImpl(
    private val publicContactsDataSource: PublicContactsDataSource,
) : PublicContactsRepository {

    override fun getAllContacts(): Flow<List<Contact>> = flowOf {
        publicContactsDataSource.getContacts()
    }

    override fun getContact(contactId: Identifier): Flow<Contact> = flowOf {
        publicContactsDataSource.getContact(contactId)
    }

    override fun findContact(contactId: Identifier): Flow<Contact?> {
        TODO("Not yet implemented")
    }

    override fun addContact(userId: Identifier): Flow<Contact> = flowOf {
        publicContactsDataSource.addContact(userId)
    }

    override fun deleteContact(contactId: Identifier): Flow<Contact> = flowOf {
        publicContactsDataSource.deleteContact(contactId)
    }

}