package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object UserAccessToUserTable : UUIDTable("access_user_users") {

    val fromUser = reference("from_user", UsersTable)
    val toUser = reference("to_user", UsersTable)

    val readProfile = bool("read_profile")
    val sentNotification = bool("sent_notification")
    val contactInfo = bool("contact_info")

}