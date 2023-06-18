package com.kekadoc.project.capybara.server.data.preparation

import com.kekadoc.project.capybara.server.data.source.database.entity.MessageEntity
import com.kekadoc.project.capybara.server.data.source.database.table.MessageForUserTable
import com.kekadoc.project.capybara.server.data.source.database.table.MessageTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Test : DataPreparation {

    override suspend fun condition(): Boolean = false

    override suspend fun prepare(): Unit = transaction() {
        MessageForUserTable.join(
            otherTable = MessageTable,
            joinType = JoinType.INNER,
            onColumn = MessageForUserTable.messageId,
            otherColumn = MessageTable.id,
        )
            .selectAll()
            .orderBy(MessageTable.date, SortOrder.DESC)
            .toList()
            .map { row -> row[MessageTable.id].value }
            .let { MessageEntity.forIds(it).orderBy(MessageTable.date to SortOrder.DESC) }
            .toList()
            .also {
                println("____${it.joinToString { it.date.toString() }}")
            }
//        MessageForUserEntity.find { MessageForUserTable.userId eq userId }
//            .limit(n = range.count, offset = range.from.toLong())
//            .map(MessageForUserEntity::message).map(MessageFactory::create)

    }

}