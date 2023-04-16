package com.kekadoc.project.capybara.server.data.model.user

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val name: String = emptyString(),
    val surname: String = emptyString(),
    val patronymic: String = emptyString(),
    val avatar: String = emptyString(),
    val about: String = emptyString(),
) {
    companion object {
        val Empty = Person()
    }
}