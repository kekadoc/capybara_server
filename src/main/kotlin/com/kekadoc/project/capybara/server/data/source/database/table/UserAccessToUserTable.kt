package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object UserAccessToUserTable : UUIDTable("access_user_users") {

    val fromUser = reference(
        name = "from_user",
        foreign = UsersTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val toUser = reference(
        name = "to_user",
        foreign = UsersTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )

    val readProfile = bool("read_profile")
    val sentNotification = bool("sent_notification")
    val contactInfo = bool("contact_info")

}