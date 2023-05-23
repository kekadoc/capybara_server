package com.kekadoc.project.capybara.server.data.source.database.table

import com.kekadoc.project.capybara.server.data.source.database.utils.textArray
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object MessageForUserTable : UUIDTable("message_for_user") {

    val messageId = reference(
        name = "message_id",
        foreign = MessageTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val userId = reference(
        name = "user_id",
        foreign = UsersTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val received = bool("received").default(false)
    val read = bool("read").default(false)
    val answerIndexes = textArray("answer_indexes").nullable().default(null)
    val fromGroup = bool("fromGroup").default(false)

}