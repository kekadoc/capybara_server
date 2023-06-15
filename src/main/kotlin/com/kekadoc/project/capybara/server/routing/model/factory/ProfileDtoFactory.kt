package com.kekadoc.project.capybara.server.routing.model.factory

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.domain.model.user.User
import com.kekadoc.project.capybara.server.routing.model.profile.ProfileDto

object ProfileDtoFactory : Factory.Single<User, ProfileDto> {

    override fun create(value: User): ProfileDto = ProfileDto(
        id = value.id,
        name = value.name,
        surname = value.surname,
        patronymic = value.patronymic,
        about = value.about,
    )

}