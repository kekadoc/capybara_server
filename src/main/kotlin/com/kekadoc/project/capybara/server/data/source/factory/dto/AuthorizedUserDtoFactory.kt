package com.kekadoc.project.capybara.server.data.source.factory.dto

import com.kekadoc.project.capybara.server.common.converter.revert
import com.kekadoc.project.capybara.server.common.factory.Factory
import com.kekadoc.project.capybara.server.data.model.User
import com.kekadoc.project.capybara.server.data.source.converter.dto.ProfileTypeDtoConverter
import com.kekadoc.project.capybara.server.data.source.network.model.AuthorizedUserDto

object AuthorizedUserDtoFactory : Factory.Single<User, AuthorizedUserDto> {

    override fun create(value: User): AuthorizedUserDto = AuthorizedUserDto(
        id = value.id,
        login = value.login,
        type = value.profile.type.revert(ProfileTypeDtoConverter),
        name = value.profile.name,
        surname = value.profile.surname,
        patronymic = value.profile.patronymic,
        avatar = value.profile.avatar,
        role = value.profile.role,
        about = value.profile.about,
        communications = value.communications.values.associate { (type, value) -> type.name to value },
        groupIds = value.groupIds,
    )

}