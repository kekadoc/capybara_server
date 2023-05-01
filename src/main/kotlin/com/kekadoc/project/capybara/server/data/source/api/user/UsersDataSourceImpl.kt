package com.kekadoc.project.capybara.server.data.source.api.user

import com.kekadoc.project.capybara.server.common.converter.convert
import com.kekadoc.project.capybara.server.common.converter.convertAll
import com.kekadoc.project.capybara.server.common.converter.revert
import com.kekadoc.project.capybara.server.common.extensions.orElse
import com.kekadoc.project.capybara.server.common.secure.Hash
import com.kekadoc.project.capybara.server.data.model.*
import com.kekadoc.project.capybara.server.data.source.DataSourceException
import com.kekadoc.project.capybara.server.data.source.converter.entity.UserEntityConverter
import com.kekadoc.project.capybara.server.data.source.database.entity.*
import com.kekadoc.project.capybara.server.data.source.database.table.*
import com.kekadoc.project.capybara.server.secure.PasswordGenerator
import com.kekadoc.project.capybara.server.secure.UserIdGenerator
import com.kekadoc.project.capybara.server.secure.UserSalt
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
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
                this.role = profile.role
                this.about = profile.about
            }
        }
            .convert(UserEntityConverter)
    }

    override suspend fun deleteUser(id: Identifier): User? = transaction {
        UserEntity.findById(id)
            ?.apply { delete() }
            ?.convert(UserEntityConverter)
    }

    override suspend fun getUserById(id: Identifier): User? = transaction {
        UserEntity.findById(id)
            ?.convert(UserEntityConverter)
    }

    override suspend fun getUsersByIds(ids: List<Identifier>): List<User> = transaction {
        UserEntity.find { UsersTable.id inList ids }
            .convertAll(UserEntityConverter)
    }

    override suspend fun getUserByLogin(login: String): User? = transaction {
        UserEntity.find { UsersTable.login eq login }
            .firstOrNull()
            ?.convert(UserEntityConverter)
    }

    override suspend fun updateUserPassword(userId: Identifier, newPassword: String): User? =
        transaction {
            UserEntity.findById(userId)
                ?.apply {
                    this.password = newPassword
                }
                ?.convert(UserEntityConverter)
        }

    override suspend fun updateUserProfile(userId: Identifier, profile: Profile): User? =
        transaction {
            UserEntity.findById(userId)
                ?.apply {
                    this.profile.apply {
                        this.type = profile.type.name
                        this.name = profile.name
                        this.surname = profile.surname
                        this.patronymic = profile.patronymic
                        this.avatar = profile.avatar
                        this.role = profile.role
                        this.about = profile.about
                    }
                }
                ?.convert(UserEntityConverter)
        }


}