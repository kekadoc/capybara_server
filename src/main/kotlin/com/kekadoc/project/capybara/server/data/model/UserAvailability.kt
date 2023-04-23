package com.kekadoc.project.capybara.server.data.model

data class UserAvailability(
    val contacts: List<Identifier>,
    val groups: List<Identifier>,
    val users: List<Identifier>,
)