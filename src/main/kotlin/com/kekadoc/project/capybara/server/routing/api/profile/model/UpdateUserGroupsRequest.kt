package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.domain.model.Identifier

data class UpdateUserGroupsRequest(
    val ids: Set<Identifier>,
)