package com.kekadoc.project.capybara.server.data.repository.distribution

import com.kekadoc.project.capybara.server.data.model.Notification
import kotlinx.coroutines.flow.Flow

interface DistributionRepository {

    fun distribute(
        notification: Notification,
    ): Flow<Unit>

}