package com.kekadoc.project.capybara.server.data.source.converter

import com.kekadoc.project.capybara.server.common.extensions.requireNotNull
import com.kekadoc.project.capybara.server.data.model.*
import com.kekadoc.project.capybara.server.data.source.database.entity.UserEntity

object UserEntityConverter {

    fun convert(entity: UserEntity): User {
        return User(
            id = entity.id.value.toString(),
            profile = Profile(
                type = Profile.Type.values().find { it.name == entity.profile.type }.requireNotNull(),
                name = entity.profile.name,
                surname = entity.profile.surname,
                patronymic = entity.profile.patronymic,
                avatar = entity.profile.avatar,
                role = entity.profile.role,
                about = entity.profile.about
            ),
            password = entity.password,
            login = entity.login,
            character = UserCharacter(
                salt0 = entity.character.salt_0,
                salt1 = entity.character.salt_1,
                salt2 = entity.character.salt_2,
                salt3 = entity.character.salt_3,
                salt4 = entity.character.salt_4,
            ),
            communications = Communications(emptyList()), // TODO:
            availability = UserAvailability(emptyList(), emptyList(), emptyList()), // TODO:
            groups = emptyList(),
        )
    }

}