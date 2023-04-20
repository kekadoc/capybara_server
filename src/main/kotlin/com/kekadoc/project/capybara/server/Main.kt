package com.kekadoc.project.capybara.server

fun main() {
//    Database.connect("jdbc:postgresql://localhost:5432/", driver = "org.postgresql.Driver",
//        user = "postgres", password = "1")
//
//    transaction {
//        // print sql to std-out
//        addLogger(StdOutSqlLogger)
//
//        SchemaUtils.create(
//            Users,
//            Profiles,
//            UserCharacters,
//            AuthTokens,
//            PushTokens,
//            Groups,
//            UsersGroups,
//            Contacts,
//            AccessUserUsers,
//            AccessUserGroups,
//            AccessUserContacts,
//            Notifications,
//            NotificationsForUsers,
//            NotificationsForGroups,
//        )
//
//        User.new {
//            this.login = "Test_Login"
//            this.password = "Test_Password"
//            this.profile = Profile.new {
//                this.name = "TestName"
//                this.surname = "TestSurname"
//                this.patronymic = "TestPatronymic"
//                this.type = "TestType"
//                this.avatar = "TestAvatar"
//                this.role = "TestRole"
//                this.about = "TestAbout"
//            }
//        }
//
//        User.all()
//            .toList()
//            .forEach {
//                println("___$it.")
//            }
//
////        Users.selectAll()
////            .toList()
////            .also {
////                println(it)
////            }
////
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