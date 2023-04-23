package com.kekadoc.project.capybara.server.routing.api.contacts

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.intercator.contacts.ContactsInteractor
import com.kekadoc.project.capybara.server.routing.api.contacts.model.CreateContactRequest
import com.kekadoc.project.capybara.server.routing.api.contacts.model.UpdateContactRequest
import com.kekadoc.project.capybara.server.routing.util.executeAuthorizedApi
import com.kekadoc.project.capybara.server.routing.util.requirePathId
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.get

fun Route.contacts() = route("/contacts") {

    //Получить список доступных контактов
    get { getAllContacts() }

    //Создание контакта
    post<CreateContactRequest> { request -> createContact(request) }

    route("/{id}") {

        //Получить детализацию контакта
        get { getContact(requirePathId()) }

        //Удаление контакта
        delete { deleteContact(requirePathId()) }

    }

}

private suspend fun PipelineContext.getAllContacts() = executeAuthorizedApi() {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ContactsInteractor>()
    val result = interactor.getContacts(
        authToken = authToken,
    )
    call.respond(result)
}

private suspend fun PipelineContext.getContact(
    contactId: String,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ContactsInteractor>()
    val result = interactor.getContact(
        authToken = authToken,
        contactId = contactId,
    )
    call.respond(result)
}

private suspend fun PipelineContext.createContact(
    request: CreateContactRequest,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ContactsInteractor>()
    val result = interactor.createContact(
        authToken = authToken,
        request = request,
    )
    call.respond(result)
}

private suspend fun PipelineContext.deleteContact(
    contactId: String,
) = executeAuthorizedApi {
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val interactor = Di.get<ContactsInteractor>()
    val result = interactor.deleteContact(
        authToken = authToken,
        contactId = contactId,
    )
    call.respond(result)
}