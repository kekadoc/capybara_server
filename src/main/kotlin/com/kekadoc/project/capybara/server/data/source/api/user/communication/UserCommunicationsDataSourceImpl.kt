package com.kekadoc.project.capybara.server.data.source.api.user.communication

import com.kekadoc.project.capybara.server.data.model.Communications
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.User
import com.kekadoc.project.capybara.server.data.source.converter.entity.UserEntityConverter
import com.kekadoc.project.capybara.server.data.source.database.entity.CommunicationEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserEntity
import com.kekadoc.project.capybara.server.data.source.database.table.CommunicationsTable
import org.jetbrains.exposed.sql.transactions.transaction

class UserCommunicationsDataSourceImpl : UserCommunicationsDataSource {

    override suspend fun updateUserCommunications(
        userId: Identifier,
        communications: Communications,
    ): User? = transaction {
        UserEntity.findById(userId)
            ?.also { userEntity ->
                val types = communications.values.toMutableList()
                CommunicationEntity.find { (CommunicationsTable.user eq userEntity.id) }
                    .forEach { entity ->
                        val new = types.find { communication -> communication.type.name == entity.type }
                        if (new == null) {
                            entity.delete()
                        } else {
                            entity.apply { value = entity.value }
                            types.remove(new)
                        }
                    }
                types.forEach { communication ->
                    CommunicationEntity.new {
                        this.user = userEntity
                        this.type = communication.type.name
                        this.value = communication.value
                    }
                }
            }
            ?.let(UserEntityConverter::convert)
    }

}