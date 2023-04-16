package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import com.kekadoc.project.capybara.server.data.model.user.Person
import com.kekadoc.project.capybara.server.data.model.user.ProfileType
import kotlinx.serialization.Serializable

@Serializable
data class CreateProfileRequest(
    val login: String = emptyString(),
    val person: Person = Person.Empty,
    val type: ProfileType = ProfileType.DEFAULT,
)