package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object UserAccessToGroupTable : UUIDTable("access_user_groups") {

    val user = reference("user", UsersTable)
    val group = reference("group", GroupsTable)

    val readInfo = bool("read_info").default(false)
    val readMembers = bool("read_members").default(false)
    val sentNotification = bool("sent_notification").default(false)

}