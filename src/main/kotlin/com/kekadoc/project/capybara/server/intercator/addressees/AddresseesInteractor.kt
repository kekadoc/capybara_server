package com.kekadoc.project.capybara.server.intercator.addressees

import com.kekadoc.project.capybara.server.routing.api.addressees.model.GetAddresseesResponse

interface AddresseesInteractor {

    suspend fun getAddresses(
        authToken: String,
    ): GetAddresseesResponse

}