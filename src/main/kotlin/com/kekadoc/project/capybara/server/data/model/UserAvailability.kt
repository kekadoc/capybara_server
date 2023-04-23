package com.kekadoc.project.capybara.server.data.model

data class UserAvailability(
    val contacts: List<Contact>,
    val groups: List<Group>,
    val users: List<User>,
)