@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kekadoc.project.capybara.server.intercator.addressees

import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.addressees.model.GetAddresseesResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class AddresseesInteractorImpl(
    private val userRepository: UsersRepository,
) : AddresseesInteractor {

    override suspend fun getAddresses(
        authToken: String,
    ): GetAddresseesResponse {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .map { user ->
                GetAddresseesResponse(
                    users = user.communications.availableAddressUsers,
                    groups = user.communications.availableAddressGroups,
                )
            }
            .single()
    }

}