package com.kekadoc.project.capybara.server.domain.model.group

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.user.User

data class Group(
    val id: Identifier,
    val name: String,
    val members: List<User>,
)