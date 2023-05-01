package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.UserCharacter
import com.kekadoc.project.capybara.server.data.source.database.entity.UserCharacterEntity

object UserCharacterEntityConverter : Converter<UserCharacterEntity, UserCharacter> {

    override fun convert(value: UserCharacterEntity) = UserCharacter(
        salt0 = value.salt_0,
        salt1 = value.salt_1,
        salt2 = value.salt_2,
        salt3 = value.salt_3,
        salt4 = value.salt_4,
    )

}