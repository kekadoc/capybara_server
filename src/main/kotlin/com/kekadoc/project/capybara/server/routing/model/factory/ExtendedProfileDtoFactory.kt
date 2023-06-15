package com.kekadoc.project.capybara.server.routing.model.factory

import com.kekadoc.project.capybara.server.common.converter.revert
import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.domain.model.user.User
import com.kekadoc.project.capybara.server.routing.model.converter.ProfileTypeDtoConverter
import com.kekadoc.project.capybara.server.routing.model.profile.ExtendedProfileDto

object ExtendedProfileDtoFactory : Factory.Single<User, ExtendedProfileDto> {

    override fun create(value: User): ExtendedProfileDto = ExtendedProfileDto(
        id = value.id,
        status = value.status,
        login = value.login,
        type = value.type.revert(ProfileTypeDtoConverter),
        name = value.name,
        surname = value.surname,
        patronymic = value.patronymic,
        about = value.about,
        communications = value.communications.values.associate { (type, value, approved) ->
            type.name to (value to approved)
        },
        groupIds = value.groupIds,
    )

}