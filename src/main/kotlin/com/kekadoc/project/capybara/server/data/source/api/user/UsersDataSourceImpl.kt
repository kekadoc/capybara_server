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
import com.kekadoc.project.capybara.server.data.source.database.table.ProfilesTable
import com.kekadoc.project.capybara.server.data.source.database.table.UsersTable
import com.kekadoc.project.capybara.server.domain.model.*
import com.kekadoc.project.capybara.server.domain.model.common.Range
import com.kekadoc.project.capybara.server.domain.model.user.Profile
import com.kekadoc.project.capybara.server.domain.model.user.User
import com.kekadoc.project.capybara.server.domain.model.user.UserStatus
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.or
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
            this.status = UserStatus.NEED_UPDATE_PASSWORD.name
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

    override suspend fun getUsers(range: Range): List<User> = transaction {
        val profiles = ProfileEntity.find {
            val nameLike = ProfilesTable.name like "%${range.query.orEmpty()}%"
            val surnameLike = ProfilesTable.surname like "%${range.query.orEmpty()}%"
            val patronymicLike = ProfilesTable.patronymic like "%${range.query.orEmpty()}%"
            val aboutLike = ProfilesTable.about like "%${range.query.orEmpty()}%"
            nameLike or surnameLike or patronymicLike or aboutLike
        }
            .orderBy(ProfilesTable.type to SortOrder.DESC)
            //.limit(n = range.count + 1 + range.from, offset = range.from.toLong())
        val users = UserEntity.find {
            val loginLike = UsersTable.login like range.query.orEmpty()
            val profileEq = UsersTable.profile inList profiles.map(ProfileEntity::id)
            loginLike or profileEq
        }
            .orderBy(UsersTable.login to SortOrder.ASC)
            .limit(n = range.count + 1 + range.from, offset = range.from.toLong())
        users.convertAll(UserEntityConverter)
    }

    override suspend fun updateUserStatus(
        userId: Identifier,
        status: UserStatus,
    ): User = transaction {
        (UserEntity.findById(userId) ?: throw UserNotFound(userId))
            .apply { this.status = status.name }
            .convert(UserEntityConverter)
    }

    override suspend fun updateUserPassword(
        userId: Identifier,
        newPassword: String,
    ): User = transaction {
        (UserEntity.findById(userId) ?: throw UserNotFound(id = userId))
            .apply {
                this.password = Hash.hash(
                    value = newPassword,
                    salt = UserSalt.get(
                        id = userId,
                        login = login,
                    ),
                )
                if (this.status == UserStatus.NEED_UPDATE_PASSWORD.name) {
                    this.status = UserStatus.ACTIVE.name
                }
            }
            .convert(UserEntityConverter)
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
                    this.about = profile.about
                }
            }
            ?.convert(UserEntityConverter)
            ?: throw UserNotFound(id = userId)
    }


}