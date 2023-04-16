package com.kekadoc.project.capybara.server

fun main() {

//    Database.connect("jdbc:postgresql://localhost:5432/", driver = "org.postgresql.Driver",
//        user = "postgres", password = "1")

//    transaction {
//        // print sql to std-out
//        addLogger(StdOutSqlLogger)
//
////        SchemaUtils.create(Users, Groups, Messages, Contacts)
////
////        Users.selectAll()
////            .toList()
////            .also {
////                println(it)
////            }
//
////        User.new {
////            this.profileLogin = "Oleg228"
////            this.profilePersonName = "Oleg"
////            this.profilePersonSurname = "Oleg"
////            this.profilePersonPatronymic = "Oleg"
////            this.profilePersonAvatar = null
////            this.profilePersonAbout = "Студент АСУ-19-1б"
////            this.profileType = ProfileType.USER
////            this.password = "1337228"
////            this.authToken = null
////            this.pushToken = null
////            this.communicationsAvailableAddressUsers = emptyList()
////            this.communicationsAvailableAddressGroups = emptyList()
////            this.communicationsAvailableContacts = emptyList()
////            this.groups = emptyList()
////        }
//
////        // insert new city. SQL: INSERT INTO Cities (name) VALUES ('St. Petersburg')
////        val stPete = City.new {
////            name = "St. Petersburg"
////        }
////
////        // 'select *' SQL: SELECT Cities.id, Cities.name FROM Cities
////        println("Cities: ${City.all()}")
//    }

    Application.init()

}

//object Users : UUIDTable("users") {
//    val profileLogin: Column<String> = text("profile_login")
//    val profileType: Column<ProfileType> = enumeration("profile_type")
//    val profilePersonName: Column<String> = text("profile_person_name")
//    val profilePersonSurname: Column<String> = text("profile_person_surname")
//    val profilePersonPatronymic: Column<String> = text("profile_person_patronymic")
//    val profilePersonAvatar: Column<String?> = text("profile_person_avatar").nullable()
//    val profilePersonAbout: Column<String> = text("profile_person_about")
//    val password: Column<String> = text("password")
//    val authToken: Column<String?> = text("authToken").nullable()
//    val pushToken: Column<String?> = text("pushToken").nullable()
//    val communicationsAvailableAddressUsers: Column<String> = text("communications_available_address_users")
//    val communicationsAvailableAddressGroups: Column<String> = text("communications_available_address_groups")
//    val communicationsAvailableContacts: Column<String> = text("communications_available_contacts")
//    val groups: Column<String> = text("groups")
//}
//
//class User(id: EntityID<UUID>) : UUIDEntity(id) {
//
//    var profileLogin by Users.profileLogin
//    var profileType by Users.profileType
//    var profilePersonName by Users.profilePersonName
//    var profilePersonSurname by Users.profilePersonSurname
//    var profilePersonPatronymic by Users.profilePersonPatronymic
//    var profilePersonAvatar by Users.profilePersonAvatar
//    var profilePersonAbout by Users.profilePersonAbout
//    var password by Users.password
//    var authToken by Users.authToken
//    var pushToken by Users.pushToken
//    var communicationsAvailableAddressUsers by Users.communicationsAvailableAddressUsers.transform(
//        { a -> a.joinToString(SEPARATOR) },
//        { str -> str.split(SEPARATOR).map { it } }
//    )
//    var communicationsAvailableAddressGroups by Users.communicationsAvailableAddressGroups.transform(
//        { a -> a.joinToString(SEPARATOR) },
//        { str -> str.split(SEPARATOR).map { it } }
//    )
//    var communicationsAvailableContacts by Users.communicationsAvailableContacts.transform(
//        { a -> a.joinToString(SEPARATOR) },
//        { str -> str.split(SEPARATOR).map { it } }
//    )
//    var groups by Users.groups.transform(
//        { a -> a.joinToString(SEPARATOR) },
//        { str -> str.split(SEPARATOR).map { it } }
//    )
//
//    companion object : UUIDEntityClass<User>(Users) {
//        const val SEPARATOR = ","
//    }
//
//}
//
//object Groups : UUIDTable("groups") {
//    val name: Column<String> = text("name")
//    val members: Column<String> = text("members")
//}
//
//class Group(id: EntityID<UUID>) : UUIDEntity(id) {
//
//    var name by Groups.name
//    var members by Groups.members.transform(
//        { a -> a.joinToString(SEPARATOR) },
//        { str -> str.split(SEPARATOR).map { it } }
//    )
//
//    companion object : UUIDEntityClass<Group>(Groups) {
//        const val SEPARATOR = ","
//    }
//
//}
//
//
//object Contacts : UUIDTable("contacts") {
//    val userContactId: Column<UUID> = uuid("user_contact_id")
//    val communications: Column<String> = text("communications")
//}
//
//class Contact(id: EntityID<UUID>) : UUIDEntity(id) {
//
//    var userContactId by Contacts.userContactId
//    val communications by Contacts.communications
//
//    companion object : UUIDEntityClass<Contact>(Contacts) {
//        const val SEPARATOR = ","
//    }
//
//}
//
