package com.kekadoc.project.capybara.server.data.source.network.model.factory

import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.domain.model.User
import com.kekadoc.project.capybara.server.data.source.network.model.converter.ProfileTypeDtoConverter
import com.kekadoc.project.capybara.server.data.source.network.model.ProfileDto

object ProfileDtoFactory : Factory.Single<User, ProfileDto> {

    override fun create(value: User): ProfileDto = ProfileDto(
        id = value.id,
        type = ProfileTypeDtoConverter.revert(value.profile.type),
        name = value.profile.name,
        surname = value.profile.surname,
        patronymic = value.profile.patronymic,
        avatar = value.profile.avatar,
        about = value.profile.about,
    )

}