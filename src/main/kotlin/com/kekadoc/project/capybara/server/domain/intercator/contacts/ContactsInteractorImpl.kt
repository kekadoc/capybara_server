package com.kekadoc.project.capybara.server.domain.intercator.contacts

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.extensions.mapElements
import com.kekadoc.project.capybara.server.data.repository.contacts.PublicContactsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.network.model.ContactDto
import com.kekadoc.project.capybara.server.data.source.network.model.converter.ContactDtoConverter
import com.kekadoc.project.capybara.server.data.source.network.model.factory.ProfileDtoFactory
import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.domain.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.domain.model.Contact
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.UserAccessToUser
import com.kekadoc.project.capybara.server.routing.api.contacts.model.*
import io.ktor.http.*
import kotlinx.coroutines.flow.*

class ContactsInteractorImpl(
    private val userRepository: UsersRepository,
    private val publicContactsRepository: PublicContactsRepository,
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
) : ContactsInteractor {

    override suspend fun getAllContacts(
        authToken: Token,
    ): GetAllContactsResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            combine(
                userRepository.getAllAccessForUser(userId = user.id)
                    .map { list ->
                        list.filter(UserAccessToUser::contactInfo)
                            .map(UserAccessToUser::toUserId)
                    }
                    .flatMapLatest(userRepository::getUsersByIds)
                    .mapElements { contactUser ->
                        ContactDto(
                            id = contactUser.id,
                            type = Contact.Type.DEFAULT.name,
                            profile = ProfileDtoFactory.create(contactUser),
                            communications = contactUser.communications.values
                                .associate { (type, value) -> type.name to value },
                        )
                    },
                publicContactsRepository.getAllContacts()
                    .mapElements(ContactDtoConverter::convert),
            ) { availableContactUsers, publicContacts -> availableContactUsers + publicContacts }
        }
        .map(::GetAllContactsResponse)
        .single()

    override suspend fun getContact(
        authToken: Token,
        contactId: Identifier,
    ): GetContactResponse = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapConcat { user ->
            combine(
                publicContactsRepository.findContact(contactId),
                userRepository.getAccessForUser(userId = user.id, forUserId = contactId)
            ) { publicContact, accessForUser: UserAccessToUser? ->
               when {
                    publicContact != null -> publicContact
                    accessForUser == null -> throw HttpException(HttpStatusCode.NotFound)
                    !accessForUser.contactInfo -> throw HttpException(HttpStatusCode.Forbidden)
                    else -> userRepository.getUserById(accessForUser.toUserId)
                        .map { user ->
                            user ?: throw HttpException(
                                statusCode = HttpStatusCode.NotFound,
                                message = "User for contact not found"
                            )
                        }
                        .map { toUser ->
                            Contact(
                                id = toUser.id,
                                user = toUser,
                                type = Contact.Type.DEFAULT
                            )
                        }
                        .single()
                }
            }
        }
        .map(ContactDtoConverter::convert)
        .map(::GetContactResponse)
        .single()

    override suspend fun getAllPublicContacts(
        authToken: Token,
    ): GetAllPublicContactsResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { publicContactsRepository.getAllContacts() }
        .mapElements(ContactDtoConverter::convert)
        .map(::GetAllPublicContactsResponseDto)
        .single()

    override suspend fun getPublicContact(
        authToken: Token,
        contactId: Identifier,
    ): GetPublicContactResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { publicContactsRepository.getContact(contactId = contactId) }
        .map(ContactDtoConverter::convert)
        .map(::GetPublicContactResponseDto)
        .single()

    override suspend fun addPublicContact(
        authToken: Token,
        request: AddPublicContactRequestDto,
    ): AddPublicContactResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { publicContactsRepository.addContact(userId = request.userId) }
        .map(ContactDtoConverter::convert)
        .map(::AddPublicContactResponseDto)
        .single()

    override suspend fun deletePublicContact(
        authToken: Token,
        contactId: Identifier,
    ): DeletePublicContactResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest { publicContactsRepository.deleteContact(contactId) }
        .map { DeletePublicContactResponseDto() }
        .single()

}