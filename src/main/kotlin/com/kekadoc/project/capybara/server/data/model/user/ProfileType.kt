package com.kekadoc.project.capybara.server.data.model.user

import kotlinx.serialization.Serializable

@Serializable
enum class ProfileType {
    USER,
    SPEAKER,
    ADMIN,
    DEFAULT,
}