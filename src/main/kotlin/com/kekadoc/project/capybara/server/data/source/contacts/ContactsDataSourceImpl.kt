package com.kekadoc.project.capybara.server.data.source.contacts

import com.google.firebase.database.FirebaseDatabase
import com.kekadoc.project.capybara.server.common.exception.EntityNotFoundException
import com.kekadoc.project.capybara.server.common.extensions.*
import com.kekadoc.project.capybara.server.data.model.CommunicationType
import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.coroutines.flow.Flow

class ContactsDataSourceImpl(
    database: FirebaseDatabase,
) : ContactsDataSource {

    private val contacts = database.getReference("/contacts")

    override fun getContacts(ids: List<Identifier>): Flow<List<Contact>> = flowOf {
        contacts.getAll<Contact>().values.toList().filterNotNull()
    }

    override fun getContact(contactId: Identifier): Flow<Contact> = flowOf {
        println("___getContact")
        contacts.getAll<Contact>().also {
            println("getALL $it")
        }
        contacts.child(contactId).get<Contact>()
            ?: throw EntityNotFoundException("Contact [$contactId] not found for get")
    }

    override fun createContact(
        userContactId: String,
        communications: Map<CommunicationType, String>,
    ): Flow<Contact> = flowOf {
        val document = contacts.push()
        val contact = Contact(
            id = document.key,
            userContactId = userContactId,
            communications = communications.mapKeys { it.key.name },
        )
        document.set(contact)
        contact
    }

    override fun updateContact(
        contactId: String,
        communications: Map<CommunicationType, String>
    ): Flow<Contact> = flowOf {
        contacts.child(contactId).runTransaction { currentContact ->
            currentContact?.copy(
                communications = communications.mapKeys { it.key.name },
            )
        } ?: throw EntityNotFoundException("Contact [$contactId] not found for update")
    }

    override fun deleteContact(contactId: String): Flow<Unit> = flowOf(contacts.child(contactId)::remove)

}