package com.kekadoc.project.capybara.server.data.source.api.user.communication

import com.kekadoc.project.capybara.server.common.exception.UserNotFound
import com.kekadoc.project.capybara.server.common.extensions.orElse
import com.kekadoc.project.capybara.server.data.source.database.entity.CommunicationEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.converter.UserEntityConverter
import com.kekadoc.project.capybara.server.data.source.database.table.CommunicationsTable
import com.kekadoc.project.capybara.server.domain.model.user.Communications
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.user.User
import org.jetbrains.exposed.sql.transactions.transaction

class UserCommunicationsDataSourceImpl : UserCommunicationsDataSource {

    override suspend fun updateUserCommunications(
        userId: Identifier,
        communications: Communications,
    ): User = transaction {
        UserEntity.findById(userId)
            .orElse { throw UserNotFound(userId) }
            .also { userEntity ->
                val types = communications.values.toMutableList()
                CommunicationEntity.find { (CommunicationsTable.user eq userEntity.id) }
                    .forEach { entity ->
                        val new = types.find { communication -> communication.type.name == entity.type }
                        if (new == null) {
                            entity.delete()
                        } else {
                            if (entity.value != new.value) {
                                entity.apply {
                                    value = new.value
                                    approved = false
                                }
                            }
                            types.remove(new)
                        }
                    }
                types.forEach { communication ->
                    CommunicationEntity.new {
                        this.user = userEntity
                        this.type = communication.type.name
                        this.value = communication.value
                        this.approved = false
                    }
                }
            }
            .let(UserEntityConverter::convert)
    }

}