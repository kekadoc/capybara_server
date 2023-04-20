package com.kekadoc.project.capybara.server.data.source.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

//
object AccessUserUsersTable : UUIDTable("access_user_users") {

    val fromUser = reference("from_user", UsersTable)
    val toUser = reference("to_user", UsersTable)

}