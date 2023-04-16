package com.kekadoc.project.capybara.server.routing.api.addressees

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.intercator.addressees.AddresseesInteractor
import com.kekadoc.project.capybara.server.routing.util.execution.get
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.get

fun Route.addressees() = route("addressees") {
    //Получение списка доступных аддресатов для сообщения
    get(ApiKeyVerifier, AuthorizationVerifier) { getAddressees() }
}

private suspend fun PipelineContext.getAddressees() {
    val interactor = Di.get<AddresseesInteractor>()
    val authToken = AuthorizationVerifier.requireAuthorizationToken()
    val result = interactor.getAddresses(authToken)
    call.respond(result)
}
