package com.kekadoc.project.capybara.server.data.source.message.db

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

//object Messages : UUIDTable("messages") {
//    val authorId: Column<UUID> = uuid("author_id")
//    //val addresseeGroups: Column<String> = text("addressee_groups")
//    //val addresseeUsers: Column<String> = text("addressee_users")
//    val contentTitle: Column<String> = text("content_title")
//    val contentText: Column<String> = text("content_text")
//    val contentImage: Column<String> = text("content_image")
//    val stateStatus: Column<String> = text("state_status")
//    val stateReadCounter: Column<Int> = integer("state_read_counter")
//    val stateReceiveCounter: Column<Int> = integer("state_receive_counter")
//}
//
//class Message(id: EntityID<UUID>) : UUIDEntity(id) {
//
//    var authorId by Messages.authorId
//    var addresseeGroups by Messages.addresseeGroups.transform(
//        { a -> a.joinToString(SEPARATOR) },
//        { str -> str.split(SEPARATOR).map { it } }
//    )
//    var addresseeUsers by Messages.addresseeUsers.transform(
//        { a -> a.joinToString(SEPARATOR) },
//        { str -> str.split(SEPARATOR).map { it } }
//    )
//    var contentTitle by Messages.contentTitle
//    var contentText by Messages.contentText
//    var contentImage by Messages.contentImage
//    var stateStatus by Messages.stateStatus
//    var stateReadCounter by Messages.stateReadCounter
//    var stateReceiveCounter by Messages.stateReceiveCounter
//
//    companion object : UUIDEntityClass<Message>(Messages) {
//        const val SEPARATOR = ","
//    }
//
//}
