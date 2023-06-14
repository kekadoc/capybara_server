package com.kekadoc.project.capybara.server.domain.model.common

data class Range(
    val from: Int,
    val count: Int,
    val query: String? = null,
)