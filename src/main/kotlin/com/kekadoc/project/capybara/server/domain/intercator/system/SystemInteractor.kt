package com.kekadoc.project.capybara.server.domain.intercator.system

import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.system.model.SystemMobileFeaturesDto

interface SystemInteractor {

    suspend fun getSystemFeatures(accessToken: Token): SystemMobileFeaturesDto

}