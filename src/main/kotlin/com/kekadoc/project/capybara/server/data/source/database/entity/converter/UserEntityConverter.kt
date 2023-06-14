package com.kekadoc.project.capybara.server.data.source.database.entity.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.data.source.database.entity.GroupEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserGroupEntity
import com.kekadoc.project.capybara.server.domain.model.user.Communications
import com.kekadoc.project.capybara.server.domain.model.user.User
import com.kekadoc.project.capybara.server.domain.model.user.UserStatus
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

object UserEntityConverter : Converter<UserEntity, User> {

    override fun convert(value: UserEntity): User = User(
        id = value.id.value,
        status = UserStatus.valueOf(value.status),
        profile = ProfileEntityConverter.convert(value.profile),
        password = value.password,
        login = value.login,
        communications = value.communications
            .map(CommunicationEntityConverter::convert)
            .let(::Communications),
        groupIds = value.groups
            .map(UserGroupEntity::group)
            .map(GroupEntity::id)
            .map(EntityID<UUID>::value),
    )

}