package com.kekadoc.project.capybara.server.test

import com.kekadoc.project.capybara.server.common.component.Component
import com.kekadoc.project.capybara.server.data.source.converter.entity.UserEntityConverter
import com.kekadoc.project.capybara.server.data.source.database.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction

object Test : Component {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun init() {
        scope.launch {
            test()
        }
    }

    private suspend fun test() = transaction {
        val newGroup = GroupEntity.new {
            name = "MyGroup"
        }
        val newUser1 = UserEntity.new {
            this.login = "User#1"
            this.password = "Test_Password"
            this.character = UserCharacterEntity.new {
                this.salt_0 = "011"
                this.salt_1 = "011"
                this.salt_2 = "011"
                this.salt_3 = "011"
                this.salt_4 = "011"
            }
            this.profile = ProfileEntity.new {
                this.name = "profile 1"
                this.surname = ""
                this.patronymic = ""
                this.type = "ADMIN"
                this.avatar = ""
                this.role = ""
                this.about = ""
            }
        }
        val newUser2 = UserEntity.new {
            this.login = "User#2"
            this.password = "Test_Password"
            this.character = UserCharacterEntity.new {
                this.salt_0 = "011"
                this.salt_1 = "011"
                this.salt_2 = "011"
                this.salt_3 = "011"
                this.salt_4 = "011"
            }
            this.profile = ProfileEntity.new {
                this.name = "profile 2"
                this.surname = ""
                this.patronymic = ""
                this.type = "ADMIN"
                this.avatar = ""
                this.role = ""
                this.about = ""
            }
        }

        UserGroupEntity.new {
            this.user = newUser1
            this.group = newGroup
        }

        AccessUserGroupEntity.new {
            this.user = newUser1
            this.group = newGroup
        }

        AccessUserUserEntity.new {
            this.fromUser = newUser1
            this.toUser = newUser2
        }

        AccessUserContactEntity.new {
            this.user = newUser1
            this.contact = newUser2
        }

        CommunicationEntity.new {
            this.type = "EMAIL"
            this.value = "ergerg@mail.ru"
            this.user = newUser1
        }

        val findUser = UserEntity.findById(id = newUser1.id)
        println("findUser ${UserEntityConverter.convert(findUser!!)}")
    }

}