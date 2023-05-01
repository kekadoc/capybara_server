package com.kekadoc.project.capybara.server.data.source.factory.dto

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.data.model.User
import com.kekadoc.project.capybara.server.data.source.converter.dto.ProfileTypeDtoConverter
import com.kekadoc.project.capybara.server.data.source.network.model.ProfileDto

object ProfileDtoFactory : Factory.Single<User, ProfileDto> {

    override fun create(value: User): ProfileDto = ProfileDto(
        id = value.id,
        type = ProfileTypeDtoConverter.revert(value.profile.type),
        name = value.profile.name,
        surname = value.profile.surname,
        patronymic = value.profile.patronymic,
        avatar = value.profile.avatar,
        role = value.profile.role,
        about = value.profile.about,
    )

}