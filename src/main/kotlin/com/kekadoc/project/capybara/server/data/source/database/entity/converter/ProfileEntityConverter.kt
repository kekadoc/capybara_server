package com.kekadoc.project.capybara.server.data.source.database.entity.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.source.database.entity.ProfileEntity
import com.kekadoc.project.capybara.server.domain.model.Profile

object ProfileEntityConverter : Converter<ProfileEntity, Profile> {

    override fun convert(value: ProfileEntity): Profile = Profile(
        type = enumValueOf(value.type),
        name = value.name,
        surname = value.surname,
        patronymic = value.patronymic,
        avatar = value.avatar,
        about = value.about,
    )

}