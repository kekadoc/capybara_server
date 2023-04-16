package com.kekadoc.project.capybara.server.intercator.contacts

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.model.user.ProfileType
import com.kekadoc.project.capybara.server.data.repository.contacts.ContactsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.contacts.model.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsInteractorImpl(
    private val userRepository: UsersRepository,
    private val contactsRepository: ContactsRepository,
) : ContactsInteractor {

    override suspend fun getContacts(
        authToken: String,
    ): GetAllContactsResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            contactsRepository.getContacts(user.communications.availableContacts)
        }
        .map(::GetAllContactsResponse)
        .single()

    override suspend fun createContact(
        authToken: String,
        request: CreateContactRequest,
    ): CreateContactResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            contactsRepository.createContact(
                userContactId = request.userContactId,
                communications = request.communications,
            )
        }
        .map(::CreateContactResponse)
        .single()

    override suspend fun getContact(
        authToken: String,
        contactId: String,
    ): GetContactResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .onEach { user ->
            if (!user.communications.availableContacts.contains(contactId))
                throw HttpException(HttpStatusCode.Forbidden)
        }
        .flatMapLatest { contactsRepository.getContact(contactId) }
        .map(::GetContactResponse)
        .single()

    override suspend fun updateContact(
        authToken: String,
        contactId: String,
        request: UpdateContactRequest
    ): UpdateContactResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .onEach { user ->
            if (user.id != contactId && user.profile.type != ProfileType.ADMIN)
                throw HttpException(HttpStatusCode.Forbidden)
        }
        .flatMapLatest {
            contactsRepository.updateContact(
                contactId = contactId,
                communications = request.communications,
            )
        }
        .map(::UpdateContactResponse)
        .single()

    override suspend fun deleteContact(
        authToken: String,
        contactId: String,
    ) = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { contactsRepository.deleteContact(contactId) }
        .single()

}