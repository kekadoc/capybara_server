package com.kekadoc.project.capybara.server.data.repository.distribution

import com.kekadoc.project.capybara.server.common.extensions.mapElements
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Notification
import com.kekadoc.project.capybara.server.data.model.User
import com.kekadoc.project.capybara.server.data.repository.group.GroupsRepository
import com.kekadoc.project.capybara.server.data.repository.notification.mobile.MobileNotificationsRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import kotlinx.coroutines.flow.*

class DistributionManagerImpl(
    private val groupsRepository: GroupsRepository,
    private val usersRepository: UsersRepository,
    private val mobileNotificationsRepository: MobileNotificationsRepository,
) : DistributionRepository {

    override fun distribute(notification: Notification): Flow<Unit> {

        val usersFromGroupsFlow = groupsRepository.getGroups(notification.addresseeGroupIds)
            .map { list -> list.map { group -> group.members }.flatten() }
            .mapElements(User::id)
            .map(List<Identifier>::distinct)

        val usersFlow = usersRepository.getUsersByIds(notification.addresseeUserIds)
            .map { list -> list.map(User::id) }
            .map(List<Identifier>::distinct)

        return combine(
            usersFromGroupsFlow,
            usersFlow
        ) { usersFromGroups, users -> (usersFromGroups + users).distinct() }
            .flatMapLatest { userIds ->
                mobileNotificationsRepository.getPushTokens(userIds)
                    .flatMapLatest { pushTokens: Map<Identifier, String?> ->
                        val sendNotificationFlows: List<Flow<Pair<Identifier, String>>> = pushTokens.map { (userId, pushToken) ->
                            if (pushToken == null) return@map null
                            mobileNotificationsRepository.sendNotification(
                                pushToken = pushToken,
                                title = notification.content.title,
                                body = notification.content.text,
                                imageUrl = notification.content.image,
                            ).map { pushId -> userId to pushId }
                        }.filterNotNull()

                        combine(sendNotificationFlows) { pushIds: Array<Pair<Identifier, String>> ->
                            pushIds.map { (userId, pushId) ->
                                mobileNotificationsRepository.savePushNotificationId(
                                    userId = userId,
                                    notificationId = notification.id,
                                    pushId = pushId,
                                )
                            }
                        }
                            .flatMapLatest { combine(it) {} }

                    }
            }
    }

}