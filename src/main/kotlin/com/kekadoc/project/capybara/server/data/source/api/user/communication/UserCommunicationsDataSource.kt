package com.kekadoc.project.capybara.server.data.source.api.user.communication

import com.kekadoc.project.capybara.server.domain.model.Communications
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.User

interface UserCommunicationsDataSource {

    suspend fun updateUserCommunications(
        userId: Identifier,
        communications: Communications,
    ): User

}