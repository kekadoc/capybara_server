package com.kekadoc.project.capybara.server.data.source.converter.entity

import com.kekadoc.project.capybara.server.data.model.*
import com.kekadoc.project.capybara.server.data.source.database.entity.*

object UserEntityConverter {

    fun convert(entity: UserEntity): User {
        return User(
            id = entity.id.value.toString(),
            profile = ProfileEntityConverter.convert(entity.profile),
            password = entity.password,
            login = entity.login,
            character = UserCharacterEntityConverter.convert(entity.character),
            communications = Communications(entity.communications.map(CommunicationEntityConverter::convert)),
            availability = UserAvailability(
                contacts = entity.availableContacts.map { accessUserContactEntity ->
                    accessUserContactEntity.contact.id.value.toString()
                },
                groups = entity.availableGroups.map { accessUserGroupEntity ->
                    accessUserGroupEntity.group.id.value.toString()
                },
                users = entity.availableUsers.map { accessUserGroupEntity ->
                    accessUserGroupEntity.toUser.id.value.toString()
                },
            ),
            groupIds = entity.groups.map(UserGroupEntity::group).map { it.id.toString() },
        )
    }

}