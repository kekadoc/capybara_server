package com.kekadoc.project.capybara.server.data

import com.kekadoc.project.capybara.server.common.component.Component
import com.kekadoc.project.capybara.server.data.preparation.AddingMockData
import com.kekadoc.project.capybara.server.data.preparation.InitAdminAccount
import io.ktor.server.application.*

object Data : Component {

    private val dataPreparationList = listOf(
        InitAdminAccount,
        AddingMockData,
    )

    override suspend fun init(application: Application) {
        dataPreparationList.forEach { preparation ->
            if (preparation.condition()) preparation.prepare()
        }
    }

}