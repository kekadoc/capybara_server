package com.kekadoc.project.capybara.server.data.source.api.contacts

import com.kekadoc.project.capybara.server.common.converter.convert
import com.kekadoc.project.capybara.server.common.converter.convertAll
import com.kekadoc.project.capybara.server.common.extensions.orElse
import com.kekadoc.project.capybara.server.common.extensions.requireNotNull
import com.kekadoc.project.capybara.server.data.model.Contact
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.source.converter.entity.PublicContactEntityConverter
import com.kekadoc.project.capybara.server.data.source.database.entity.PublicContactEntity
import com.kekadoc.project.capybara.server.data.source.database.entity.UserEntity
import com.kekadoc.project.capybara.server.data.source.database.table.PublicContactsTable
import org.jetbrains.exposed.sql.transactions.transaction

class PublicContactsDataSourceImpl : PublicContactsDataSource {

    override suspend fun getContacts(): List<Contact> = transaction {
        PublicContactEntity.all().convertAll(PublicContactEntityConverter)
    }

    override suspend fun getContact(contactId: Identifier): Contact? = transaction {
        PublicContactEntity.findById(contactId)?.convert(PublicContactEntityConverter)
    }

    override suspend fun addContact(userId: Identifier): Contact = transaction {
        val user = UserEntity.findById(userId).requireNotNull { "User not found" }
        PublicContactEntity.find { PublicContactsTable.user eq userId }
            .find { it.user.id.value == userId }
            .orElse {
                PublicContactEntity.new {
                    this.user = user
                }
            }
            .convert(PublicContactEntityConverter)
    }

    override suspend fun deleteContact(contactId: Identifier): Contact? = transaction {
        PublicContactEntity.findById(contactId)
            ?.also { entity -> entity.delete() }
            ?.convert(PublicContactEntityConverter)
    }

}