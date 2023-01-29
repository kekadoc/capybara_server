package com.kekadoc.project.capybara.server.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Organization(
    val name: String? = null,
    val department: String? = null
) {
    companion object {
        val Empty = Organization()
    }
}