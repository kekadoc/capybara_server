package com.kekadoc.project.capybara.server.data.source.api.user

import com.kekadoc.project.capybara.server.common.converter.convert
import com.kekadoc.project.capybara.server.common.converter.convertAll
import com.kekadoc.project.capybara.server.common.exception.UserNotFound
import com.kekadoc.project.capybara.server.common.secure.Hash
import com.kekadoc.project.capybara.server.common.secure.UserIdGenerator
import com.kekadoc.project.capybara.server.common.secure.UserSalt
import com.kekadoc.project.capybara.server.data.source.database.entity.ProfileEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.converter.UserEntityConverter
import com.kekadoc.project.capybara.server.data.source.database.table.UsersTable
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Profile
import com.kekadoc.project.capybara.server.domain.model.User
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UsersDataSourceImpl : UsersDataSource {

    override suspend fun createUser(
        login: String,
        password: String,
        profile: Profile,
    ): User = transaction {
        val userId = UsersTable.id.defaultValueFun?.invoke()?.value ?: UUID.fromString(
            UserIdGenerator.generate()
        )
        UserEntity.new(id = userId) {
            this.login = login
            this.password = Hash.hash(
                value = password,
                salt = UserSalt.get(
                    id = userId,
                    login = login,
                ),
            )
            this.profile = ProfileEntity.new {
                this.name = profile.name
                this.surname = profile.surname
                this.patronymic = profile.patronymic
                this.type = profile.type.name
                this.avatar = profile.avatar
                this.about = profile.about
            }
        }.convert(UserEntityConverter)
    }

    override suspend fun deleteUser(id: Identifier): User = transaction {
        UserEntity.findById(id)
            ?.apply { delete() }
            ?.convert(UserEntityConverter)
            ?: throw UserNotFound(id)
    }

    override suspend fun getUserById(id: Identifier): User = transaction {
        UserEntity.findById(id)?.convert(UserEntityConverter) ?: throw UserNotFound(id)
    }

    override suspend fun getUsersByIds(ids: List<Identifier>): List<User> =
        findUsersByIds(ids)
            .also { list ->
                ids.forEach { userId ->
                    if (list.find { user -> user.id == userId } == null) throw UserNotFound(userId)
                }
            }

    override suspend fun findUsersByIds(ids: List<Identifier>): List<User> = transaction {
        UserEntity.find { UsersTable.id inList ids }
            .convertAll(UserEntityConverter)
    }

    override suspend fun findUserByLogin(login: String): User? = transaction {
        UserEntity.find { UsersTable.login eq login }
            .firstOrNull()
            ?.convert(UserEntityConverter)
    }

    override suspend fun updateUserPassword(
        userId: Identifier,
        newPassword: String,
    ): User = transaction {
        UserEntity.findById(userId)
            ?.apply { this.password = newPassword }
            ?.convert(UserEntityConverter)
            ?: throw UserNotFound(id = userId)
    }

    override suspend fun updateUserProfile(
        userId: Identifier,
        profile: Profile,
    ): User = transaction {
        UserEntity.findById(userId)
            ?.apply {
                this.profile.apply {
                    this.type = profile.type.name
                    this.name = profile.name
                    this.surname = profile.surname
                    this.patronymic = profile.patronymic
                    this.avatar = profile.avatar
                    this.about = profile.about
                }
            }
            ?.convert(UserEntityConverter)
            ?: throw UserNotFound(id = userId)
    }


}