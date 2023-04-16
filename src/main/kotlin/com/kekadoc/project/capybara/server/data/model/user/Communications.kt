package com.kekadoc.project.capybara.server.data.model.user

import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.serialization.Serializable

@Serializable
data class Communications(
    val availableAddressUsers: List<Identifier> = emptyList(),
    val availableAddressGroups: List<Identifier> = emptyList(),
    val availableContacts: List<Identifier> = emptyList(),
) {

    companion object {
        val Empty: Communications = Communications(
            availableAddressUsers = emptyList(),
            availableAddressGroups = emptyList(),
            availableContacts = emptyList(),
        )
    }

}