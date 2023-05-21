package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object UserAccessToGroupTable : UUIDTable("access_user_groups") {

    val user = reference(
        name = "user",
        foreign = UsersTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val group = reference(
        name = "group",
        foreign = GroupsTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )

    val readInfo = bool("read_info").default(false)
    val readMembers = bool("read_members").default(false)
    val sentNotification = bool("sent_notification").default(false)

}