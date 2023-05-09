package com.kekadoc.project.capybara.server.routing.verifier

import io.ktor.server.application.*

object ProfileVerifier : Verifier {

//    context(ApplicationCall)
//    private suspend fun requireUser(): User {
//        val token = AuthorizationVerifier.requireAuthorizationToken()
//        val authRepository = Di.get<UsersRepository>()
//        return authRepository.getUserByToken(token).first() ?: throw HttpException(HttpStatusCode.Unauthorized)
//    }

    override suspend fun verify(call: ApplicationCall) {
        //call.apply { requireUser() }
    }

}