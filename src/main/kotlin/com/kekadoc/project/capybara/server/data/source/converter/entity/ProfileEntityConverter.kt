package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.source.database.entity.ProfileEntity

object ProfileEntityConverter : Converter<Profile, ProfileEntity> {

    override fun convert(source: ProfileEntity): Profile {
        return Profile(
            type = enumValueOf(source.type),
            name = source.name,
            surname = source.surname,
            patronymic = source.patronymic,
            avatar = source.avatar,
            role = source.role,
            about = source.about,
        )
    }

}