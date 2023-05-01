@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.kekadoc.project.capybara.server.intercator.addressees

import com.kekadoc.project.capybara.server.data.model.access.UserAccessToGroup
import com.kekadoc.project.capybara.server.data.model.access.UserAccessToUser
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.converter.dto.GroupDtoConverter
import com.kekadoc.project.capybara.server.data.source.factory.dto.ProfileDtoFactory
import com.kekadoc.project.capybara.server.data.source.network.model.AddresseeDto
import com.kekadoc.project.capybara.server.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.routing.api.addressees.model.GetAddresseesResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.single

class AddresseesInteractorImpl(
    private val userRepository: UsersRepository,
    private val groupsRepository: GroupsRepository,
    private val fetchUserByAccessTokenFunction: FetchUserByAccessTokenFunction,
) : AddresseesInteractor {

    override suspend fun getAddresses(
        authToken: String,
    ): GetAddresseesResponse {
        return fetchUserByAccessTokenFunction.fetchUser(authToken)
            .requireAuthorizedUser()
            .flatMapLatest { user ->
                combine(
                    userRepository.getAllAccessForUser(userId = user.id),
                    userRepository.getAllAccessForGroup(userId = user.id),
                ) { userAccessList, groupAccessList ->
                    val usersList = userAccessList
                        .filter(UserAccessToUser::sentNotification)
                        .map(UserAccessToUser::toUserId)
                    val groupsList = groupAccessList
                        .filter(UserAccessToGroup::sentNotification)
                        .map(UserAccessToGroup::groupId)
                    combine(
                        userRepository.getUsersByIds(usersList),
                        groupsRepository.getGroups(groupsList),
                    ) { users, groups ->
                        GetAddresseesResponse(
                            users = users
                                .map { nextUser ->
                                    AddresseeDto(
                                        userId = nextUser.id,
                                        profile = ProfileDtoFactory.create(nextUser),
                                    )
                                },
                            groups = groups
                                .map(GroupDtoConverter::convert),
                        )
                    }
                }.flattenConcat()
            }
            .single()
    }

}