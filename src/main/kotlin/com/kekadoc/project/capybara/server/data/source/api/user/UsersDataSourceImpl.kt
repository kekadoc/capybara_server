package com.kekadoc.project.capybara.server.data.source.api.user

import com.kekadoc.project.capybara.server.common.extensions.flowOf
import com.kekadoc.project.capybara.server.data.model.Communications
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.model.User
import com.kekadoc.project.capybara.server.data.source.converter.UserEntityConverter
import com.kekadoc.project.capybara.server.data.source.database.entity.ProfileEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserCharacterEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UsersDataSourceImpl : UsersDataSource {

    override fun createUser(login: String, profile: Profile): Flow<User> {
        return flowOf {
            transaction {
                val character = UserCharacterEntity.new {
                    this.salt_0 = "011"
                    this.salt_1 = "011"
                    this.salt_2 = "011"
                    this.salt_3 = "011"
                    this.salt_4 = "011"
                }
                UserEntity.new {
                    this.login = login
                    this.password = "Test_Password"
                    this.character = character
                    this.profile = ProfileEntity.new {
                        this.name = profile.name
                        this.surname = profile.surname
                        this.patronymic = profile.patronymic
                        this.type = profile.type.name
                        this.avatar = profile.avatar
                        this.role = profile.role
                        this.about = profile.about
                    }
                }.let(UserEntityConverter::convert)
            }
        }
    }

    override fun deleteUser(id: String): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun getUserById(id: String): Flow<User?> {
        return flowOf {
            transaction {
                UserEntity.findById(UUID.fromString(id))
            }?.let { entity ->
                UserEntityConverter.convert(entity)
            }
        }
    }

    override fun getUserByToken(token: String): Flow<User?> {
        TODO("Not yet implemented")
    }

    override fun getUserByLogin(login: String): Flow<User?> {
        TODO("Not yet implemented")
    }

    override fun updateUserPassword(userId: Identifier, password: String): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserProfile(userId: Identifier, profile: Profile): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserCommunications(
        userId: Identifier,
        communications: Communications,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserAvailabilityGroupsAdd(
        userId: Identifier,
        groupIds: Set<Identifier>,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserAvailabilityGroupsRemove(
        userId: Identifier,
        groupIds: Set<Identifier>,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserAvailabilityUsersAdd(
        userId: Identifier,
        userIds: Set<Identifier>,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserAvailabilityUsersRemove(
        userId: Identifier,
        userIds: Set<Identifier>,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserAvailabilityContactsAdd(
        userId: Identifier,
        contactIds: Set<Identifier>,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserAvailabilityContactsRemove(
        userId: Identifier,
        contactIds: Set<Identifier>,
    ): Flow<User> {
        TODO("Not yet implemented")
    }


}