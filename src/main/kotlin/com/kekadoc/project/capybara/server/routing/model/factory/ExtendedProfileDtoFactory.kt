package com.kekadoc.project.capybara.server.routing.model.factory

import com.kekadoc.project.capybara.server.common.converter.revert
import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.routing.model.profile.ExtendedProfileDto
import com.kekadoc.project.capybara.server.routing.model.converter.ProfileTypeDtoConverter
import com.kekadoc.project.capybara.server.domain.model.user.User

object ExtendedProfileDtoFactory : Factory.Single<User, ExtendedProfileDto> {

    override fun create(value: User): ExtendedProfileDto = ExtendedProfileDto(
        id = value.id,
        status = value.status,
        login = value.login,
        type = value.profile.type.revert(ProfileTypeDtoConverter),
        name = value.profile.name,
        surname = value.profile.surname,
        patronymic = value.profile.patronymic,
        about = value.profile.about,
        communications = value.communications.values.associate { (type, value, approved) ->
            type.name to (value to approved)
        },
        groupIds = value.groupIds,
    )

}