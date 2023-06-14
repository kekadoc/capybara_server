package com.kekadoc.project.capybara.server.routing.model.factory

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.routing.model.profile.ProfileDto
import com.kekadoc.project.capybara.server.domain.model.user.User

object ProfileDtoFactory : Factory.Single<User, ProfileDto> {

    override fun create(value: User): ProfileDto = ProfileDto(
        id = value.id,
        name = value.profile.name,
        surname = value.profile.surname,
        patronymic = value.profile.patronymic,
        about = value.profile.about,
    )

}