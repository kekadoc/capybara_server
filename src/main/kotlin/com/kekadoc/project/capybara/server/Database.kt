package com.kekadoc.project.capybara.server

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp
import java.util.*

//
object Users : UUIDTable("users") {

    val login = varchar("login", 255)
    val password = varchar("password", 255)
    val profile = reference("profile", Profiles)

}

class User(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<User>(Users)

    var login by Users.login
    var password by Users.password
    var profile by Profile referencedOn Users.profile

    override fun toString(): String {
        return "User(login='$login', password='$password', profile=$profile)"
    }

}

//
object Profiles : UUIDTable("profiles") {

    val type = varchar("type", 255)
    val name = text("name")
    val surname = text("surname")
    val patronymic = text("patronymic")
    val avatar = text("avatar")
    val role = text("role")
    val about = text("about")

}

class Profile(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<Profile>(Profiles)

    var type by Profiles.type
    var name by Profiles.name
    var surname by Profiles.surname
    var patronymic by Profiles.patronymic
    var avatar by Profiles.avatar
    var role by Profiles.role
    var about by Profiles.about

}

//
object UserCharacters : UUIDTable("user_characters") {

    val user = reference("user", Users)
    val salt_0 = varchar("salt_0", 255)
    val salt_1 = varchar("salt_1", 255)
    val salt_2 = varchar("salt_2", 255)
    val salt_3 = varchar("salt_3", 255)
    val salt_4 = varchar("salt_4", 255)

}

class UserCharacter(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<UserCharacter>(UserCharacters)

    var user by User referencedOn UserCharacters.user
    var salt_0 by UserCharacters.salt_0
    var salt_1 by UserCharacters.salt_1
    var salt_2 by UserCharacters.salt_2
    var salt_3 by UserCharacters.salt_3
    var salt_4 by UserCharacters.salt_4

}

//
object AuthTokens : UUIDTable("auth_tokens") {

    val user = reference("user", Users)
    val token = varchar("token", 255)
    val createdAt = timestamp("createdAt").defaultExpression(CurrentTimestamp())

}

class AuthToken(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<AuthToken>(AuthTokens)

    var user by User referencedOn AuthTokens.user
    var token by AuthTokens.token
    var createdAt by AuthTokens.createdAt

}

//
object PushTokens : UUIDTable("push_tokens") {

    val user = reference("user", Users)
    val token = varchar("token", 255)

}

class PushToken(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<PushToken>(PushTokens)

    var user by User referencedOn PushTokens.user
    var token by PushTokens.token

}

//
object Groups : UUIDTable("groups") {

    val name = varchar("name", 255)

}

class Group(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<Group>(Groups)

    var name by Groups.name

}

//
object UsersGroups : UUIDTable("users_groups") {

    val user = reference("user", Users)
    val group = reference("group", Groups)

}

class UserGroup(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<UserGroup>(UsersGroups)

    var user by User referencedOn UsersGroups.user
    var group by Group referencedOn UsersGroups.group

}

//
object Contacts : UUIDTable("contacts") {

    val user = reference("user", Users)
    val communications = text("communications")

}

class Contact(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<Contact>(Contacts)

    var user by User referencedOn Contacts.user
    var communications by Contacts.communications

}

//
object AccessUserUsers : UUIDTable("access_user_users") {

    val fromUser = reference("from_user", Users)
    val toUser = reference("to_user", Users)

}

class AccessUserUser(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<AccessUserUser>(AccessUserUsers)

    var fromUser by User referencedOn AccessUserUsers.fromUser
    var toUser by User referencedOn AccessUserUsers.toUser

}

//
object AccessUserGroups : UUIDTable("access_user_groups") {

    val user = reference("user", Users)
    val group = reference("group", Groups)

}

class AccessUserGroup(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<AccessUserGroup>(AccessUserGroups)

    var user by User referencedOn AccessUserGroups.user
    var group by Group referencedOn AccessUserGroups.group

}

//
object AccessUserContacts : UUIDTable("access_user_contacts") {

    val user = reference("user", Users)
    val contact = reference("contact", Contacts)

}

class AccessUserContact(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<AccessUserContact>(AccessUserContacts)

    var user by User referencedOn AccessUserContacts.user
    var contact by Contact referencedOn AccessUserContacts.contact

}

//
object Notifications : UUIDTable("notifications") {

    val author = reference("author", Users)
    val type = varchar("type", 255)
    val contentTitle: Column<String> = varchar("content_title", 255)
    val contentText: Column<String> = varchar("content_text", 255)
    val contentImage: Column<String> = varchar("content_image", 255)
    val status: Column<String> = varchar("status", 255)

}

class Notification(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<Notification>(Notifications)

    var author by User referencedOn Notifications.author
    var type by Notifications.type
    var contentTitle by Notifications.contentTitle
    var contentText by Notifications.contentText
    var contentImage by Notifications.contentImage
    var status by Notifications.status

}

//
object NotificationsForUsers : UUIDTable("notification_users") {

    val notificationId = reference("notification_id", Notifications)
    val userId = reference("user_id", Users)
    val received = bool("received")
    val read = bool("read")
    val answer = bool("answer")

}

class NotificationsForUser(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<NotificationsForUser>(NotificationsForUsers)

    var notificationId by Notification referencedOn NotificationsForUsers.notificationId
    var userId by User referencedOn NotificationsForUsers.userId
    var received by NotificationsForUsers.received
    var read by NotificationsForUsers.read
    var answer by NotificationsForUsers.answer
}

//
object NotificationsForGroups : UUIDTable("notifications_groups") {

    val notificationId = reference("notification_id", Notifications)
    val group = reference("group", Groups)

}

class NotificationsForGroup(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<NotificationsForGroup>(NotificationsForGroups)

    var notificationId by Notification referencedOn NotificationsForGroups.notificationId
    var group by Group referencedOn NotificationsForGroups.group

}