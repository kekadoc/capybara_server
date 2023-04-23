package com.kekadoc.project.capybara.server.intercator.addressees

import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.converter.dto.GroupDtoConverter
import com.kekadoc.project.capybara.server.data.source.converter.dto.ProfileDtoConverter
import com.kekadoc.project.capybara.server.data.source.network.model.AddresseeDto
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.addressees.model.GetAddresseesResponse
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.single

class AddresseesInteractorImpl(
    private val userRepository: UsersRepository,
    private val groupsRepository: GroupsRepository,

) : AddresseesInteractor {

    override suspend fun getAddresses(
        authToken: String,
    ): GetAddresseesResponse {
        return userRepository.getUserByToken(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                combine(
                    userRepository.getUsersByIds(user.availability.users),
                    groupsRepository.getGroups(user.availability.groups)
                ) { users, groups ->
                    GetAddresseesResponse(
                        users = users
                            .map { nextUser ->
                                AddresseeDto(
                                    userId = nextUser.id,
                                    profile = ProfileDtoConverter.revert(nextUser.profile),
                                )
                            },
                        groups = groups
                            .map(GroupDtoConverter::revert),
                    )
                }

            }
            .single()
    }

}