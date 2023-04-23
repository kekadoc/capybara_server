package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.UserCharacter
import com.kekadoc.project.capybara.server.data.source.database.entity.UserCharacterEntity

object UserCharacterEntityConverter : Converter<UserCharacter, UserCharacterEntity> {

    override fun convert(source: UserCharacterEntity) = UserCharacter(
        salt0 = source.salt_0,
        salt1 = source.salt_1,
        salt2 = source.salt_2,
        salt3 = source.salt_3,
        salt4 = source.salt_4,
    )

}