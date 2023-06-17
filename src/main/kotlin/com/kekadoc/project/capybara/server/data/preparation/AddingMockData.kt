package com.kekadoc.project.capybara.server.data.preparation

import com.kekadoc.project.capybara.server.Config
import com.kekadoc.project.capybara.server.data.function.create_user.CreateUserFunction
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.local.LocalDataSource
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.group.Group
import com.kekadoc.project.capybara.server.domain.model.user.CreatedUser
import com.kekadoc.project.capybara.server.domain.model.user.Profile
import com.kekadoc.project.capybara.server.domain.model.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.koin.core.component.get

object AddingMockData : DataPreparation {

    override suspend fun condition(): Boolean {
        val localDataSource = Di.get<LocalDataSource>()
        return Config.isDebugCreateMockData && !localDataSource.getDebugCreateMockData()
    }

    override suspend fun prepare() {

        val localDataSource = Di.get<LocalDataSource>()
        val usersRepository = Di.get<UsersRepository>()
        val groupsRepository = Di.get<GroupsRepository>()
        val createUserFunction = Di.get<CreateUserFunction>()

        val testTeacherFlow = createUserFunction.invoke(
            login = "TesterTeacher",
            type = Profile.Type.SPEAKER,
            name = "Тестовый",
            surname = "Обычный",
            patronymic = "Преподаватель",
            about = randomTeacherRank(),
        )

        val testGroupFlow = createUserFunction.invoke(
            login = "TesterStudent",
            type = Profile.Type.USER,
            name = "Тестовый",
            surname = "Обычный",
            patronymic = "Студент",
            about = "Студент",
        ).flatMapConcat { testUser ->
            groupsRepository.createGroup(
                name = "Тестовая Группа",
                type = Group.Type.STUDENT,
                members = setOf(testUser.user.id),
            )
        }

        val teachersFlow = List(10) {
            createUserFunction.invoke(
                type = Profile.Type.SPEAKER,
                name = randomName(),
                surname = randomSurname(),
                patronymic = randomSurname(),
                about = randomTeacherRank(),
            )
        }
            .plus(testTeacherFlow)
            .combine()

        val teachersGroup = teachersFlow.flatMapConcat { teachers ->
            groupsRepository.createGroup(
                name = "Педогогический совет",
                type = Group.Type.OTHER,
                members = teachers.map { it.user.id }.toSet(),
            )
        }

        groups.map { groupName ->
            List(10) {
                createUserFunction.invoke(
                    type = Profile.Type.USER,
                    name = randomName(),
                    surname = randomSurname(),
                    patronymic = randomPatronymic(),
                    about = "Студент",
                )
            }
                .combine()
                .mapElements(CreatedUser::user)
                .mapElements(User::id)
                .map(List<Identifier>::toSet)
                .flatMapConcat { members ->
                    groupsRepository.createGroup(
                        name = groupName,
                        type = Group.Type.STUDENT,
                        members = members,
                    )
                }
        }
            .plus(teachersGroup)
            .plus(testGroupFlow)
            .merge()
            .flowOn(Dispatchers.IO)
            .collect()

        localDataSource.setDebugCreateMockData(true)
    }

}

fun randomGroupName(): String = groups.random()
fun randomName(): String = peopleList.random().firstName
fun randomSurname(): String = peopleList.random().lastName
fun randomPatronymic(): String = peopleList.random().middleName
fun randomTeacherRank(): String = teacherRanks.random()

private data class Person(
    val lastName: String,
    val firstName: String,
    val middleName: String,
)

val teacherRanks = listOf(
    "д-р техн.наук, профессор",
    "канд.техн.наук, доцент",
    "канд.филос.наук, доцент",
    "старший преподаватель",
    "канд.пед.наук, доцент",
    "канд экон.наук, доцент",
    "канд.экон.наук, доцент",
    "канд.физмат.наук, доцент",
    "канд.психол.наук, доцент",
    "канд.техн.наук, доцент",
    "канд.социол.наук, доцент",
    "доцент",
)

val groups = listOf(
    "АСУ-22-1б-ЧФ",
    "ЭС-22-1б-ЧФ",
    "ПГС-22-1б-ЧФ",
    "АСУ-21-1б-ЧФ",
    "АТПП-21-1б-ЧФ",
    "ПГС-21-1б-ЧФ",
    "АСУ-20-1б-ЧФ",
    "АТПП-20-1б-ЧФ",
    "ЭС-20-1б-ЧФ",
    "АСУ-19-1б-ЧФ",
    "АТПП-19-1б-ЧФ",
    "ЭС-19-1б-ЧФ",
    "ЭС-22-1бозЧФ",
    "АТПП-20-1бозЧФ",
    "ЭС-20-1бозЧФ",
    "АТПП-19-1бозЧФ",
    "ЭС-19-1бозЧФ",
    "АТПП-18-1бозЧФ",
    "ЭС-18-1бозЧФ",
    "АСУ-22з",
    "АСУ-21з",
    "АСУ-20з",
    "АСУ-19з",
    "ЭС-22з",
    "ЭС-21з",
    "ЭС-20з",
    "ЭС-19з",
    "ЭС-18з",
    "БТПП-22з",
    "ПГС-20з",
    "АТПП-21з",
    "АТПП-20з",
    "АТПП-19з",
    "АТПП-18з",
)

private val peopleList = listOf(
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