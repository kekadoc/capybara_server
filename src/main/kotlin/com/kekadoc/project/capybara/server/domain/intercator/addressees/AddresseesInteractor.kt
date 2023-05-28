package com.kekadoc.project.capybara.server.domain.intercator.addressees

import com.kekadoc.project.capybara.server.routing.api.addressees.model.GetAddresseesResponseDto

interface AddresseesInteractor {

    suspend fun getAddresses(
        authToken: String,
    ): GetAddresseesResponseDto

}