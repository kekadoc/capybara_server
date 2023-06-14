package com.kekadoc.project.capybara.server.data.repository.contacts

import com.kekadoc.project.capybara.server.domain.model.user.Contact
import com.kekadoc.project.capybara.server.domain.model.Identifier
import kotlinx.coroutines.flow.Flow

interface PublicContactsRepository {

    fun getAllContacts(): Flow<List<Contact>>

    fun getContact(contactId: Identifier): Flow<Contact>

    fun findContact(contactId: Identifier): Flow<Contact?>

    fun addContact(
        userId: Identifier,
    ): Flow<Contact>

    fun deleteContact(
        contactId: Identifier,
    ): Flow<Contact>

}