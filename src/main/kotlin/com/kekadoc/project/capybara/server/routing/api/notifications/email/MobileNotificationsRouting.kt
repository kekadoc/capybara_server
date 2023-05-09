package com.kekadoc.project.capybara.server.routing.api.notifications.email

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.intercator.email_message.EmailMessageInteractor
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.execution.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.get

fun Route.emailNotifications() = route("/email") {

    route("/answer") {

        get { sentAnswerFromEmail() }

    }

}

private suspend fun PipelineContext.sentAnswerFromEmail(

) = execute {
    val answerToken = this.call.parameters["at"]
    if (answerToken == null) {
        call.respond(status = HttpStatusCode.BadRequest, "Invalid request")
    } else {
        val interactor = Di.get<EmailMessageInteractor>()
        val result = interactor.sentAnswerFromEmail(answerToken)
        call.respond(result)
    }
}