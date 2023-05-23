package com.kekadoc.project.capybara.server.domain.intercator.functions

import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.domain.model.User
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.routing.model.message.ReceivedMessagePreviewDto
import com.kekadoc.project.capybara.server.routing.model.factory.ReceivedMessagePreviewDtoFactory
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.single

class GetReceivedMessageFunction(
    private val messagesRepository: MessagesRepository,
    private val usersRepository: UsersRepository,
) {

    suspend fun get(message: Message, addresseeUser: User,): ReceivedMessagePreviewDto {
        return combine(
            messagesRepository.getAddresseeUserInfo(message.id, userId = addresseeUser.id),
            usersRepository.getUserById(message.authorId)
        ) { forUser, author -> ReceivedMessagePreviewDtoFactory.invoke(message, forUser, author) }
            .single()
    }

}