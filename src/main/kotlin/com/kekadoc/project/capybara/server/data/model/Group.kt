package com.kekadoc.project.capybara.server.data.model

import kotlinx.serialization.Serializable

/**
 * @param id Идентификатор
 * @param members Идентификаторы участников
 * @param messages Идентификатор хранилища сообщений
 */
@Serializable
data class Group(
    val id: Identifier,
    val members: Set<Identifier>,
    val messages: Identifier
)