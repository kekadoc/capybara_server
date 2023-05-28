@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.kekadoc.project.capybara.server.domain.intercator.addressees

import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.domain.intercator.functions.FetchUserByAccessTokenFunction
import com.kekadoc.project.capybara.server.domain.intercator.requireAuthorizedUser
import com.kekadoc.project.capybara.server.domain.model.UserAccessToGroup
import com.kekadoc.project.capybara.server.domain.model.UserAccessToUser
import com.kekadoc.project.capybara.server.routing.api.addressees.model.GetAddresseesResponseDto
import com.kekadoc.project.capybara.server.routing.model.converter.GroupDtoConverter
import com.kekadoc.project.capybara.server.routing.model.factory.ProfileDtoFactory
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
    ): GetAddresseesResponseDto {
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
                        GetAddresseesResponseDto(
                            users = users.map(ProfileDtoFactory::create),
                            groups = groups.map(GroupDtoConverter::convert),
                        )
                    }
                }.flattenConcat()
            }
            .single()
    }

}