package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.source.database.entity.ProfileEntity

object ProfileEntityConverter : Converter<ProfileEntity, Profile> {

    override fun convert(value: ProfileEntity): Profile = Profile(
        type = enumValueOf(value.type),
        name = value.name,
        surname = value.surname,
        patronymic = value.patronymic,
        avatar = value.avatar,
        role = value.role,
        about = value.about,
    )

}