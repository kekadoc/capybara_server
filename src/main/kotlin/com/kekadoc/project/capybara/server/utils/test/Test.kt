package com.kekadoc.project.capybara.server.utils.test

import com.kekadoc.project.capybara.server.common.component.Component
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.model.common.Range
import io.ktor.server.application.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import org.koin.core.component.get

object Test : Component {

    override fun init(application: Application) {
        if (false) {
            GlobalScope.launch {
                val usersRepository = Di.get<UsersRepository>()
                val groupsRepository = Di.get<GroupsRepository>()
                val messagesRepository = Di.get<MessagesRepository>()

//                usersRepository.createUser(
//                    login = "OlegAdmin",
//                    password = "123",
//                    profile = Profile(
//                        type = Profile.Type.ADMIN,
//                        name = "Oleg",
//                        surname = "Korelin",
//                        patronymic = "Sergeervich",
//                        about = null,
//                    ),
//                ).collect()

                usersRepository.getUsers(Range(5, 500))
                    .flatMapConcat {
                        it.map { it.id }.map { usersRepository.deleteUser(it) }.merge()
                    }
                    .collect()

//                repeat(100) {
//                    val profile = peopleList.random()
//                    usersRepository.createUser(
//                        login = "User#$it",
//                        password = "123",
//                        profile = Profile(
//                            type = Profile.Type.USER,
//                            name = profile.firstName,
//                            surname = profile.lastName,
//                            patronymic = profile.middleName,
//                            about = "Студент группы ${Random.nextInt(1, 20)}",
//                        ),
//                    ).collect()
//                }
//                repeat(100) {
//                    usersRepository.createUser(
//                        login = "User#$it",
//                        password = "123",
//                        profile = Profile(
//                            type = Profile.Type.USER,
//                            name = "Oleg",
//                            surname = "Korelin",
//                            patronymic = "Sergeervich",
//                            about = null,
//                        ),
//                    ).collect()
//                }
//                messagesRepository.getMessagesByAuthorId(
//                    UUID.fromString("a477e8db-4269-4dbb-806c-8b65d1e5289d"),
//                    Range(0, 100, ""),
//                ).flatMapConcat { messages ->
//                        messages.map { message ->
//                            messagesRepository.removeMessage(message.id)
//                        }.merge()
//                    }.collect()

            }
        }

    }

}

data class Person(val lastName: String, val firstName: String, val middleName: String)

val peopleList = listOf(
    Person("Иванов", "Иван", "Иванович"),
    Person("Петров", "Петр", "Петрович"),
    Person("Сидоров", "Сидор", "Сидорович"),
    Person("Лебедева", "Чеслава", "Олеговна"),
    Person("Доронина", "Августина", "Кимовна"),
    Person("Суханова", "Роксалана", "Святославовна"),
    Person("Кабанова", "Капитолина", "Демьяновна"),
    Person("Устинова", "Светлана", "Германовна"),
    Person("Русакова", "Гелена", "Ильяовна"),
    Person("Петрова", "Арина", "Матвеевна"),
    Person("Антонова", "Лигия", "Наумовна"),
    Person("Иванкова", "Харитина", "Константиновна"),
    Person("Титова", "Илена", "Альбертовна"),
    Person("Сорокина", "Георгина", "Олеговна"),
    Person("Кошелева", "Эмилия", "Романовна"),
    Person("Харитонова", "Бронислава", "Наумовна"),
    Person("Ермакова", "Мэри", "Лукьевна"),
    Person("Зиновьева", "Алиса", "Авксентьевна"),
    Person("Журавлёва", "Харитина", "Эльдаровна"),
    Person("Якушева", "Эльвира", "Евсеевна"),
    Person("Киселёва", "Эльвира", "Викторовна"),
    Person("Котова", "Ярослава", "Михайловна"),
    Person("Мартынова", "Юстина", "Тимуровна"),
    Person("Никитин", "Мечеслав", "Артемович"),
    Person("Семёнов", "Ибрагил", "Иринеевич"),
    Person("Цветков", "Вальтер", "Аристархович"),
    Person("Комиссаров", "Григорий", "Геннадиевич"),
    Person("Никифоров", "Руслан", "Ефимович"),
    Person("Орлов", "Гарри", "Улебович"),
    Person("Морозов", "Святослав", "Парфеньевич"),
    Person("Сафонов", "Фрол", "Гордеевич"),
    Person("Зиновьев", "Людвиг", "Адольфович"),
    Person("Авдеев", "Гордий", "Германнович"),
    Person("Бобылёв", "Платон", "Михайлович"),
    Person("Дементьев", "Лука", "Макарович"),
    Person("Попов", "Альфред", "Натанович"),
    Person("Калинин", "Эрик", "Кимович"),
    Person("Фокин", "Май", "Алексеевич"),
    Person("Мышкин", "Макар", "Максимович"),
    Person("Князев", "Исаак", "Кириллович"),
    Person("Евсеев", "Оскар", "Иосифович"),
    Person("Федосеев", "Лукьян", "Рудольфович"),
    Person("Коновалов", "Гаянэ", "Егорович"),
    // и т.д.
)






