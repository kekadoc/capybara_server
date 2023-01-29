package com.kekadoc.project.capybara.server.data.model

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String = emptyString(),
    val login: String = emptyString(),
    val person: Person = Person.Empty,
    val organization: Organization = Organization.Empty,
    val type: ProfileType = ProfileType.UNKNOWN
) {
    companion object {
        val Empty = Profile()
    }
}