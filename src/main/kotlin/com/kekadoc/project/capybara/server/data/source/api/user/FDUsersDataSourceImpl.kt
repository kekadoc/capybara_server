package com.kekadoc.project.capybara.server.data.source.api.user

import com.google.firebase.database.FirebaseDatabase
import com.kekadoc.project.capybara.server.data.model.Communications
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Profile
import com.kekadoc.project.capybara.server.data.model.User
import kotlinx.coroutines.flow.Flow

class FDUsersDataSourceImpl(
    database: FirebaseDatabase,
) : UsersDataSource {

    private val users = database.getReference("/users")

    override fun createUser(login: String, profile: Profile): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun deleteUser(id: String): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun getUserById(id: String): Flow<User?> {
        TODO("Not yet implemented")
    }

    override fun getUsersByIds(id: List<Identifier>): Flow<List<User>> {
        TODO("Not yet implemented")
    }

    override fun getUserByToken(token: String): Flow<User?> {
        TODO("Not yet implemented")
    }

    override fun getUserByLogin(login: String): Flow<User?> {
        TODO("Not yet implemented")
    }

    override fun updateUserPassword(userId: Identifier, password: String): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserProfile(userId: Identifier, profile: Profile): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserCommunications(
        userId: Identifier,
        communications: Communications,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserAvailabilityGroupsAdd(
        userId: Identifier,
        groupIds: Set<Identifier>,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserAvailabilityGroupsRemove(
        userId: Identifier,
        groupIds: Set<Identifier>,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserAvailabilityUsersAdd(
        userId: Identifier,
        userIds: Set<Identifier>,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserAvailabilityUsersRemove(
        userId: Identifier,
        userIds: Set<Identifier>,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserAvailabilityContactsAdd(
        userId: Identifier,
        contactIds: Set<Identifier>,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUserAvailabilityContactsRemove(
        userId: Identifier,
        contactIds: Set<Identifier>,
    ): Flow<User> {
        TODO("Not yet implemented")
    }

//    override fun createUser(
//        login: String,
//        person: Profile,
//    ): Flow<User> = flowOf {
//        val document = users.push()
//        val user = User(
//            id = document.key,
//            profile = Profile(
//                login = login,
//                person = person,
//                type = type,
//            ),
//            password = UUID.randomUUID().toString().take(8),
//        )
//        document.set(user)
//        user
//    }
//
//    override fun getUserById(id: String): Flow<User?> = flowOf {
//        users.child(id).get<User>()
//    }
//
//    override fun getUserByToken(token: String): Flow<User?> = flowOf {
//        users.orderByChild("authToken")
//            .equalTo(token)
//            .getAll<User>()
//            .values
//            .firstOrNull()
//    }
//
//    override fun getUserByLogin(login: String): Flow<User?> = flowOf {
//        users.orderByChild("profile/login")
//            .equalTo(login)
//            .getAll<User>()
//            .values
//            .firstOrNull()
//    }
//
//    override fun updateUserPassword(
//        id: String,
//        password: String,
//    ): Flow<User> = flowOf {
//        users.child(id).runTransaction<User> { currentUser ->
//            currentUser?.copy(
//                password = password,
//            )
//        } ?: throw UserNotRegisteredException("User for change password not found")
//    }
//
//    override fun updateUserProfile(userId: Identifier, profile: Profile): Flow<User> = flowOf {
//        users.child(id).runTransaction<User> { currentUser ->
//            currentUser?.copy(
//                profile = currentUser.profile.copy(
//                    person = person,
//                ),
//            )
//        } ?: throw UserNotRegisteredException("User for change personal not found")
//    }
//
//    override fun updateUserProfileType(
//        id: String,
//        type: ProfileType,
//    ): Flow<User> = flowOf {
//        users.child(id).runTransaction<User> { currentUser ->
//            currentUser?.copy(
//                profile = currentUser.profile.copy(
//                    type = type,
//                ),
//            )
//        } ?: throw UserNotRegisteredException("User for change profile type not found")
//    }
//
//    override fun updateUserPushToken(
//        id: String,
//        pushToken: String,
//    ): Flow<User> = flowOf {
//        users.child(id).runTransaction<User> { currentUser ->
//            currentUser?.copy(
//                pushToken = pushToken,
//            )
//        } ?: throw UserNotRegisteredException("User for change push token not found")
//    }
//
//    override fun updateUserAuthToken(
//        id: String,
//        authToken: String,
//    ): Flow<User> = flowOf {
//        users.child(id).runTransaction<User> { currentUser ->
//            currentUser?.copy(
//                authToken = authToken,
//            )
//        } ?: throw UserNotRegisteredException("User for change auth token not found")
//    }
//
//    override fun updateUserGroupAdd(
//        id: String,
//        groups: Set<Identifier>,
//    ): Flow<User> = flowOf {
//        users.child(id).runTransaction<User> { currentUser ->
//            currentUser?.copy(
//                groups = (currentUser.groups + groups).distinct(),
//            )
//        } ?: throw UserNotRegisteredException("User for change auth token not found")
//    }
//
//    override fun updateUserGroupRemove(
//        id: String,
//        groups: Set<Identifier>,
//    ): Flow<User> = flowOf {
//        users.child(id).runTransaction<User> { currentUser ->
//            currentUser?.copy(
//                groups = (currentUser.groups - groups).distinct(),
//            )
//        } ?: throw UserNotRegisteredException("User for change auth token not found")
//    }
//
//    override fun updateUserCommunications(
//        id: String,
//        communications: Communications,
//    ): Flow<User> = flowOf {
//        users.child(id).runTransaction<User> { currentUser ->
//            currentUser?.copy(
//                communications = communications,
//            )
//        } ?: throw UserNotRegisteredException("User for change communications not found")
//    }
//
//    override fun updateUserAvailability(
//        userId: Identifier,
//        availability: UserAvailability,
//    ): Flow<User> {
//        TODO("Not yet implemented")
//    }
//
//    override fun authorizeUser(
//        login: String,
//        password: String,
//        pushToken: String,
//    ): Flow<User> = getUserByLogin(login)
//        .map { user -> user ?: throw UserNotRegisteredException() }
//        .map { user ->
//            val authToken = Token.generate()
//            users.child(user.id).runTransaction<User> { currentUser ->
//                currentUser?.copy(
//                    authToken = authToken,
//                    pushToken = pushToken,
//                )
//            } ?: throw UserNotRegisteredException()
//        }
//
//    override fun deleteUser(id: String): Flow<Unit> = flowOf { users.child(id).remove() }
//
}