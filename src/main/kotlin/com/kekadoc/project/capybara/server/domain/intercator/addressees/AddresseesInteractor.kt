package com.kekadoc.project.capybara.server.domain.intercator.addressees

import com.kekadoc.project.capybara.server.routing.api.addressees.model.GetAddresseesResponse

interface AddresseesInteractor {

    suspend fun getAddresses(
        authToken: String,
    ): GetAddresseesResponse

}