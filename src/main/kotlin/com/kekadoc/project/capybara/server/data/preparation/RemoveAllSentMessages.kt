package com.kekadoc.project.capybara.server.data.preparation

import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.model.common.Range
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.merge
import org.koin.core.component.get
import java.util.*

object RemoveAllSentMessages : DataPreparation {

    private val authorId = "2f66166b-e2d1-4b64-8ff1-1029e2425beb"

    override suspend fun condition(): Boolean = false

    override suspend fun prepare() {
        val messagesRepository = Di.get<MessagesRepository>()

        messagesRepository.getMessagesByAuthorId(
                authorId = UUID.fromString(authorId),
                range = Range(0, Int.MAX_VALUE),
            )
            .flatMapConcat { messages ->
                messages
                    .map { message -> messagesRepository.removeMessage(message.id) }
                    .merge()
            }
            .collect()
    }

}