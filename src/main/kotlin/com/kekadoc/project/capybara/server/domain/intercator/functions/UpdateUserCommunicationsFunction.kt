package com.kekadoc.project.capybara.server.domain.intercator.functions

import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.service.email.EmailDataService
import com.kekadoc.project.capybara.server.domain.model.user.Communication
import com.kekadoc.project.capybara.server.domain.model.user.Communications
import com.kekadoc.project.capybara.server.domain.model.user.User
import com.kekadoc.project.capybara.server.routing.api.profile.model.UpdateUserCommunicationsRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class UpdateUserCommunicationsFunction(
    private val userRepository: UsersRepository,
    private val emailDataService: EmailDataService,
) {

    fun update(
        user: User,
        request: UpdateUserCommunicationsRequest,
    ): Flow<User> = flow {
        val currentCommunications = user.communications.values
        val newCommunications = request.values.map { (type, value) ->
            Communication(
                type = type,
                value = value,
            )
        }
        val newEmail: String? = run {
            val findEmailPredicate: (Communication) -> Boolean = { communication ->
                communication.type == Communication.Type.Email
            }
            val newEmail = newCommunications.find(findEmailPredicate)?.value
            val currentEmail = currentCommunications.find(findEmailPredicate)?.value
            if (
                (currentEmail == null && newEmail != null)
                || (currentEmail != null && newEmail != null && currentEmail != newEmail)
            ) {
                newEmail
            } else {
                null
            }
        }
        userRepository.updateUserCommunications(
            userId = user.id,
            communications = Communications(newCommunications),
        )
            .onEach {
                println("_LOG_$newEmail")
                if (newEmail != null) {
                    emailDataService.sentConfirmEmail(
                        user = user,
                        email = newEmail,
                    )
                }

            }
            .apply { emitAll(this) }
    }

}