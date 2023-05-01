package com.kekadoc.project.capybara.server.data.source.network.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.source.database.table.NotificationsForUsersTable
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

@Serializable
data class AddresseeUserDto(
    @Contextual
    @SerialName("user_id")
    val userId: Identifier,
    @SerialName("received")
    val received: Boolean,
    @SerialName("read")
    val read: Boolean,
    @SerialName("answer")
    val answer: String?,
)