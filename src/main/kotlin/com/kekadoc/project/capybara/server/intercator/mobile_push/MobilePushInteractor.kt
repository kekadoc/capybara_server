package com.kekadoc.project.capybara.server.intercator.mobile_push

import com.kekadoc.project.capybara.server.routing.api.profile.model.UpdatePushTokenRequest

interface MobilePushInteractor {

    suspend fun updatePushTokenByAuthToken(
        authToken: String,
        request: UpdatePushTokenRequest,
    )

    suspend fun deletePushTokenByAuthToken(
        authToken: String,
    )

    suspend fun deletePushTokenById(
        authToken: String,
        profileId: String,
    )

}
