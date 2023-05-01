package com.kekadoc.project.capybara.server.data.source.api.user.communication

import com.kekadoc.project.capybara.server.data.model.Communications
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.User

interface UserCommunicationsDataSource {

    suspend fun updateUserCommunications(
        userId: Identifier,
        communications: Communications,
    ): User?

}