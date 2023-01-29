package com.kekadoc.project.capybara.server.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class ProfileType {
    USER,
    ADMIN,
    UNKNOWN
}