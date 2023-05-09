package com.kekadoc.project.capybara.server.domain.intercator.mobile_push

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.notifications.mobile.model.GetNotificationMobilePushTokenResponseDto
import com.kekadoc.project.capybara.server.routing.api.notifications.mobile.model.UpdatePushTokenRequest

interface MobilePushInteractor {

    suspend fun getPushTokenByAuth(
        authToken: Token,
    ): GetNotificationMobilePushTokenResponseDto

    suspend fun updatePushTokenByAuth(
        authToken: Token,
        request: UpdatePushTokenRequest,
    )

    suspend fun deletePushTokenByAuth(
        authToken: Token,
    )

    suspend fun updatePushTokenByUserId(
        authToken: Token,
        userId: Identifier,
        pushToken: Token?,
    )

}
