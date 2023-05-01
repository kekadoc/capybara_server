package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.model.*
import com.kekadoc.project.capybara.server.data.source.database.entity.*
import java.util.*

object UserEntityConverter : Converter<UserEntity, User> {

    override fun convert(value: UserEntity): User {
        return User(
            id = value.id.value,
            profile = ProfileEntityConverter.convert(value.profile),
            password = value.password,
            login = value.login,
            communications = Communications(value.communications.map(CommunicationEntityConverter::convert)),
            groupIds = value.groups.map(UserGroupEntity::group).map { it.id.value },
        )
    }

}