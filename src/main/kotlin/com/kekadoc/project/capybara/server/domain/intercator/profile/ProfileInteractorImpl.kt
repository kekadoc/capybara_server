package com.kekadoc.project.capybara.server.domain.intercator.profile

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.service.email.EmailDataService
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.user.Communication
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class ProfileInteractorImpl(
    private val emailDataService: EmailDataService,
    private val usersRepository: UsersRepository,
) : ProfileInteractor {

    override suspend fun confirmEmail(confirmationToken: Token): String {
        val checkTokenResult = emailDataService.checkEmailConfirmation(confirmationToken)
            ?: throw HttpException(HttpStatusCode.BadRequest)
        return usersRepository.getUserById(checkTokenResult.id)
            .nullable()
            .onErrorEmitNull()
            .flatMapConcat { user ->
                if (user == null) throw HttpException(HttpStatusCode.NotFound)
                else {
                    usersRepository.updateUserCommunications(
                        user.id,
                        communications = user.communications.copy(
                            values = user.communications.values.map { communication ->
                                if (communication.type == Communication.Type.Email
                                    && communication.value == checkTokenResult.email) {
                                    communication.copy(approved = true)
                                } else {
                                    communication
                                }
                            }
                        )
                    )
                }
            }
            .flowOn(Dispatchers.IO)
            .map { "Электронная почта успешно подтверждена \u2705" }
            .single()
    }

}