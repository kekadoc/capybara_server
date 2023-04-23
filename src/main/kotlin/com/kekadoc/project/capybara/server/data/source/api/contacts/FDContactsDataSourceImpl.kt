package com.kekadoc.project.capybara.server.data.source.api.contacts

import com.google.firebase.database.FirebaseDatabase
import com.kekadoc.project.capybara.server.common.exception.EntityNotFoundException
import com.kekadoc.project.capybara.server.common.extensions.flowOf
import com.kekadoc.project.capybara.server.common.extensions.get
import com.kekadoc.project.capybara.server.common.extensions.getAll
import com.kekadoc.project.capybara.server.common.extensions.remove
import com.kekadoc.project.capybara.server.data.model.Communications
import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.coroutines.flow.Flow

class FDContactsDataSourceImpl(
    database: FirebaseDatabase,
) : ContactsDataSource {

    private val contacts = database.getReference("/contacts")

    override fun getContacts(ids: List<Identifier>): Flow<List<Contact>> = flowOf {
        contacts.getAll<Contact>().values.toList().filterNotNull()
    }

    override fun getContact(contactId: Identifier): Flow<Contact> = flowOf {
        contacts.child(contactId).get<Contact>()
            ?: throw EntityNotFoundException("Contact [$contactId] not found for get")
    }

    override fun createContact(
        userContactId: Identifier,
        communications: Communications
    ): Flow<Contact> = flowOf {
        throw NotImplementedError()
//        val document = contacts.push()
//        val contact = Contact(
//            id = document.key,
//            userContactId = userContactId,
//            communications = communications.mapKeys { it.key.name },
//        )
//        document.set(contact)
//        contact
    }

    override fun updateContact(
        contactId: Identifier,
        communications: Communications
    ): Flow<Contact> = flowOf {
        throw NotImplementedError()
//        contacts.child(contactId).runTransaction { currentContact ->
//            currentContact?.copy(
//                communications = communications.mapKeys { it.key.name },
//            )
//        } ?: throw EntityNotFoundException("Contact [$contactId] not found for update")
    }

    override fun deleteContact(contactId: String): Flow<Unit> = flowOf(contacts.child(contactId)::remove)

}