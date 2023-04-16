package com.kekadoc.project.capybara.server.data.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Addressee {

    @Serializable
    data class User(
        val id: Identifier,
    ) : Addressee()

    @Serializable
    data class Group(
        val id: Identifier,
        val members: List<Identifier>,
    ) : Addressee()

}
