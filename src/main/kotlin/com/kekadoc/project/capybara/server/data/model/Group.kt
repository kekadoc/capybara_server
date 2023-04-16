package com.kekadoc.project.capybara.server.data.model

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import kotlinx.serialization.Serializable

/**
 * @param id Идентификатор
 * @param members Идентификаторы участников
 */
@Serializable
data class Group(
    val id: Identifier = emptyString(),
    val name: String = emptyString(),
    val members: List<Identifier> = emptyList(),
)