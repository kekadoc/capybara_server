package com.kekadoc.project.capybara.server.intercator.mobile_push

import com.kekadoc.project.capybara.server.data.repository.notification.mobile.MobileNotificationsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.intercator.requireAdminUser
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.profile.model.UpdatePushTokenRequest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class MobilePushInteractorImpl(
    private val usersRepository: UsersRepository,
    private val mobileNotificationsRepository: MobileNotificationsRepository,
) : MobilePushInteractor {

    override suspend fun updatePushTokenByAuthToken(
        authToken: String,
        request: UpdatePushTokenRequest,
    ) = usersRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            mobileNotificationsRepository.setUserPushToken(
                userId = user.id,
                pushToken = request.token,
            )
        }
        .map { }
        .single()

    override suspend fun deletePushTokenByAuthToken(
        authToken: String,
    ) = usersRepository.getUserByToken(authToken)
        .requireAuthorizedUser()
        .flatMapLatest { user ->
            mobileNotificationsRepository.deleteUserPushToken(
                userId = user.id,
            )
        }
        .map { }
        .single()


    override suspend fun deletePushTokenById(
        authToken: String,
        profileId: String,
    ) {
        usersRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .requireAdminUser()
            .flatMapLatest {
                mobileNotificationsRepository.deleteUserPushToken(
                    userId = profileId,
                )
            }
            .map { }
            .single()
    }

}