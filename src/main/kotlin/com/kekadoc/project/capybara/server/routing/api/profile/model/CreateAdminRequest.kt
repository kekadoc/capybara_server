package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import com.kekadoc.project.capybara.server.data.model.user.Person
import kotlinx.serialization.Serializable

@Serializable
data class CreateAdminRequest(
    val login: String = emptyString(),
    val person: Person = Person.Empty,
    val password: String = emptyString(),
)