@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kekadoc.project.capybara.server.intercator.mobile_push

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Token
import com.kekadoc.project.capybara.server.data.repository.notification.mobile.MobileNotificationsRepository
import com.kekadoc.project.capybara.server.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.notifications.mobile.model.GetNotificationMobilePushTokenResponseDto
import com.kekadoc.project.capybara.server.routing.api.notifications.mobile.model.UpdatePushTokenRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import java.util.*

class MobilePushInteractorImpl(
    private val mobileNotificationsRepository: MobileNotificationsRepository,
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
) : MobilePushInteractor {

    override suspend fun getPushTokenByAuth(
        authToken: Token
    ): GetNotificationMobilePushTokenResponseDto = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            mobileNotificationsRepository.getPushToken(
                userId = user.id,
            )
        }
        .map(::GetNotificationMobilePushTokenResponseDto)
        .single()

    override suspend fun updatePushTokenByAuth(
        authToken: Token,
        request: UpdatePushTokenRequest,
    ) = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            mobileNotificationsRepository.setPushToken(
                userId = user.id,
                pushToken = request.token,
            )
        }
        .collect()

    override suspend fun deletePushTokenByAuth(
        authToken: Token,
    ) = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            mobileNotificationsRepository.deletePushToken(
                userId = user.id,
            )
        }
        .collect()

    override suspend fun updatePushTokenByUserId(
        authToken: Token,
        userId: Identifier,
        pushToken: Token?,
    ): Unit = fetchUserByAccessTokenFunction.fetchUser(authToken)
        .requireAuthorizedUser()
        .requireAdminUser()
        .flatMapLatest {
            if (pushToken == null) {
                mobileNotificationsRepository.deletePushToken(
                    userId = userId,
                )
            } else {
                mobileNotificationsRepository.setPushToken(
                    userId = userId,
                    pushToken = pushToken,
                )
            }
        }
        .collect()

}