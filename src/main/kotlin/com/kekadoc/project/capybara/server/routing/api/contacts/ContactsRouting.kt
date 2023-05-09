package com.kekadoc.project.capybara.server.routing.api.contacts

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.intercator.contacts.ContactsInteractor
import com.kekadoc.project.capybara.server.routing.api.contacts.model.*
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.executeAuthorizedApi
import com.kekadoc.project.capybara.server.routing.util.requirePathId
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.get

fun Route.contacts() = route("/contacts") {

    /**
     * Получить список всех доступных контактов
     * Request: Unit
     * Response: [GetAllContactsResponse]
     */
    get { getAllContacts() }

    /**
     * Контакт
     */
    route("/{id}") {

        /**
         * Получить детализацию контакта
         * Request: contactId
         * Response: [GetContactResponse]
         */
        get { execute { getContact(requirePathId()) } }

    }

    /**
     * Публичные контакты, доступные всем авторизованным пользователям
     */
    route("/public") {

        /**
         * Список всех публичных контактов
         * Request: Unit
         * Response: [GetAllPublicContactsResponseDto]
         */
        get { getAllPublicContacts() }

        /**
         * Создание публичного контакта
         * Request: [AddPublicContactRequestDto]
         * Response: [AddPublicContactResponseDto]
         */
        post<AddPublicContactRequestDto> { request -> addPublicContact(request) }

        route("/{id}") {

            /**
             * Получение одного публичного контакта
             * Request: contactId
             * Response: [GetPublicContactResponseDto]
             */
            get { execute { getPublicContact(requirePathId()) } }

            /**
             * Удаление контакта из публичного доступа
             * Request: contactId
             * Response: Unit
             */
            delete { execute { deletePublicContact(requirePathId()) } }

        }

    }

}

private suspend fun PipelineContext.getAllContacts() = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ContactsInteractor>()
    val result = interactor.getAllContacts(
        authToken = authToken,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getContact(
    contactId: Identifier,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ContactsInteractor>()
    val result = interactor.getContact(
        authToken = authToken,
        contactId = contactId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getAllPublicContacts() = executeAuthorizedApi() {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ContactsInteractor>()
    val result = interactor.getAllPublicContacts(
        authToken = authToken,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getPublicContact(
    contactId: Identifier,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ContactsInteractor>()
    val result = interactor.getPublicContact(
        authToken = authToken,
        contactId = contactId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.addPublicContact(
    request: AddPublicContactRequestDto,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ContactsInteractor>()
    val result = interactor.addPublicContact(
        authToken = authToken,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.deletePublicContact(
    contactId: Identifier,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ContactsInteractor>()
    val result = interactor.deletePublicContact(
        authToken = authToken,
        contactId = contactId,
    )
    call.respond(result)
}
