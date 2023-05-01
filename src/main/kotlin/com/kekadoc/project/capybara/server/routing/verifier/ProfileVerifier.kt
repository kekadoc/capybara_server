package com.kekadoc.project.capybara.server.routing.verifier

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.model.User
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.di.Di
import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.coroutines.flow.first
import org.koin.core.component.get

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