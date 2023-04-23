package com.kekadoc.project.capybara.server.intercator.contacts

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.repository.contacts.PublicContactsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.converter.dto.CommunicationsDtoConverter
import com.kekadoc.project.capybara.server.data.source.converter.dto.ContactDtoConverter
import com.kekadoc.project.capybara.server.data.source.converter.dto.ProfileDtoConverter
import com.kekadoc.project.capybara.server.data.source.network.model.ContactDto
import com.kekadoc.project.capybara.server.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.contacts.model.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsInteractorImpl(
    private val userRepository: UsersRepository,
    private val publicContactsRepository: PublicContactsRepository,
) : ContactsInteractor {

    override suspend fun getContacts(
        authToken: String,
    ): GetAllContactsResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            combine(
                userRepository.getUsersByIds(user.availability.contacts),
                publicContactsRepository.getContacts(),
            ) { availableContactUsers, publicContacts ->
                (availableContactUsers + publicContacts.map(Contact::user))
                    .map { contactUser ->
                        ContactDto(
                            id = contactUser.id,
                            profile = ProfileDtoConverter.revert(contactUser.profile),
                            communications = CommunicationsDtoConverter.convert(contactUser.communications),
                        )
                    }
            }
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
            publicContactsRepository.createContact(
                userContactId = request.userContactId,
                communications = request.communications
                    .let(CommunicationsDtoConverter::revert),
            )
        }
        .map(ContactDtoConverter::convert)
        .map(::CreateContactResponse)
        .single()

    override suspend fun getContact(
        authToken: String,
        contactId: String,
    ): GetContactResponse = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .onEach { user ->
            if (!user.availability.contacts.any { id -> id == contactId })
                throw HttpException(HttpStatusCode.Forbidden)
        }
        .flatMapLatest { publicContactsRepository.getContact(contactId) }
        .map(ContactDtoConverter::convert)
        .map(::GetContactResponse)
        .single()

    override suspend fun deleteContact(
        authToken: String,
        contactId: String,
    ) = userRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { publicContactsRepository.deleteContact(contactId) }
        .single()

}