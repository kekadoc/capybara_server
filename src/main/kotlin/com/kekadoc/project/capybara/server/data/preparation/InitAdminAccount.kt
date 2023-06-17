package com.kekadoc.project.capybara.server.data.preparation

import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.local.LocalDataSource
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.model.common.Range
import com.kekadoc.project.capybara.server.domain.model.user.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.koin.core.component.get

object InitAdminAccount : DataPreparation {

    override suspend fun condition(): Boolean = true

    override suspend fun prepare() {
        val usersRepository = Di.get<UsersRepository>()
        val localDataSource = Di.get<LocalDataSource>()
        usersRepository.getUsers(Range(0, 1))
            .flatMapConcat { users ->
                if (users.isEmpty()) {
                    usersRepository.createUser(
                        login = "Admin",
                        password = "090301",
                        profile = Profile(
                            type = Profile.Type.ADMIN,
                            name = "Системный",
                            surname = "Товарищ",
                            patronymic = "Администратор",
                            about = "Admin",
                        ),
                    )
                        .onEach {
                            localDataSource.setDebugCreateMockData(false)
                        }
                } else {
                    flowOf(Unit)
                }
            }
            .flowOn(Dispatchers.IO)
            .collect()
    }

}