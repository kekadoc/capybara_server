package com.kekadoc.project.capybara.server.domain.intercator.email_message

interface EmailMessageInteractor {

    suspend fun sentAnswerFromEmail(answerToken: String)

}