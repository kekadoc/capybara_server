package com.kekadoc.project.capybara.server.domain.intercator.system

import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.system.SystemMobileFeatures
import com.kekadoc.project.capybara.server.domain.model.user.Communication
import com.kekadoc.project.capybara.server.routing.api.system.model.SystemMobileFeaturesDto
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class SystemInteractorImpl(
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
) : SystemInteractor {

    override suspend fun getSystemFeatures(
        accessToken: Token,
    ): SystemMobileFeaturesDto = fetchUserByAccessTokenFunction.fetchUser(accessToken)
        .requireAuthorizedUser()
        .map {
            SystemMobileFeatures(
                changeProfile = false,
                resetPassword = false,
                registration = true,
            )
        }
        .map { (changeProfile, resetPassword, registration) ->
            SystemMobileFeaturesDto(
                changeProfile = changeProfile,
                resetPassword = resetPassword,
                registration = registration,
                availableCommunications = listOf(
                    Communication.Type.Email.name,
                ),
            )
        }
        .single()

}