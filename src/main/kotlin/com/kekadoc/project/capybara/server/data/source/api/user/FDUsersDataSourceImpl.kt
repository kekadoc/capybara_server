package com.kekadoc.project.capybara.server.data.source.api.user

import com.google.firebase.database.FirebaseDatabase
import com.kekadoc.project.capybara.server.common.Token
import com.kekadoc.project.capybara.server.common.extensions.*
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.user.*
import com.kekadoc.project.capybara.server.routing.api.auth.errors.UserNotRegisteredException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class FDUsersDataSourceImpl(
    database: FirebaseDatabase,
) : UsersDataSource {

    private val users = database.getReference("/users")

    override fun createUser(
        login: String,
        person: Person,
        type: ProfileType,
    ): Flow<User> = flowOf {
        val document = users.push()
        val user = User(
            id = document.key,
            profile = Profile(
                login = login,
                person = person,
                type = type,
            ),
            password = UUID.randomUUID().toString().take(8),
        )
        document.set(user)
        user
    }

    override fun getUserById(id: String): Flow<User?> = flowOf {
        users.child(id).get<User>()
    }
    
    override fun getUserByToken(token: String): Flow<User?> = flowOf {
        users.orderByChild("authToken")
            .equalTo(token)
            .getAll<User>()
            .values
            .firstOrNull()
    }

    override fun getUserByLogin(login: String): Flow<User?> = flowOf {
        users.orderByChild("profile/login")
            .equalTo(login)
            .getAll<User>()
            .values
            .firstOrNull()
    }

    override fun updateUserPassword(
        id: String,
        password: String,
    ): Flow<User> = flowOf {
        users.child(id).runTransaction<User> { currentUser ->
            currentUser?.copy(
                password = password,
            )
        } ?: throw UserNotRegisteredException("User for change password not found")
    }

    override fun updateUserPersonal(
        id: String,
        person: Person,
    ): Flow<User> = flowOf {
        users.child(id).runTransaction<User> { currentUser ->
            currentUser?.copy(
                profile = currentUser.profile.copy(
                    person = person,
                ),
            )
        } ?: throw UserNotRegisteredException("User for change personal not found")
    }

    override fun updateUserProfileType(
        id: String,
        type: ProfileType,
    ): Flow<User> = flowOf {
        users.child(id).runTransaction<User> { currentUser ->
            currentUser?.copy(
                profile = currentUser.profile.copy(
                    type = type,
                ),
            )
        } ?: throw UserNotRegisteredException("User for change profile type not found")
    }

    override fun updateUserPushToken(
        id: String,
        pushToken: String,
    ): Flow<User> = flowOf {
        users.child(id).runTransaction<User> { currentUser ->
            currentUser?.copy(
                pushToken = pushToken,
            )
        } ?: throw UserNotRegisteredException("User for change push token not found")
    }

    override fun updateUserAuthToken(
        id: String,
        authToken: String,
    ): Flow<User> = flowOf {
        users.child(id).runTransaction<User> { currentUser ->
            currentUser?.copy(
                authToken = authToken,
            )
        } ?: throw UserNotRegisteredException("User for change auth token not found")
    }

    override fun updateUserGroupAdd(
        id: String,
        groups: Set<Identifier>,
    ): Flow<User> = flowOf {
        users.child(id).runTransaction<User> { currentUser ->
            currentUser?.copy(
                groups = (currentUser.groups + groups).distinct(),
            )
        } ?: throw UserNotRegisteredException("User for change auth token not found")
    }

    override fun updateUserGroupRemove(
        id: String,
        groups: Set<Identifier>,
    ): Flow<User> = flowOf {
        users.child(id).runTransaction<User> { currentUser ->
            currentUser?.copy(
                groups = (currentUser.groups - groups).distinct(),
            )
        } ?: throw UserNotRegisteredException("User for change auth token not found")
    }

    override fun updateUserCommunications(
        id: String,
        communications: Communications,
    ): Flow<User> = flowOf {
        users.child(id).runTransaction<User> { currentUser ->
            currentUser?.copy(
                communications = communications,
            )
        } ?: throw UserNotRegisteredException("User for change communications not found")
    }

    override fun authorizeUser(
        login: String,
        password: String,
        pushToken: String,
    ): Flow<User> = getUserByLogin(login)
        .map { user -> user ?: throw UserNotRegisteredException() }
        .map { user ->
            val authToken = Token.generate()
            users.child(user.id).runTransaction<User> { currentUser ->
                currentUser?.copy(
                    authToken = authToken,
                    pushToken = pushToken,
                )
            } ?: throw UserNotRegisteredException()
        }

    override fun deleteUser(id: String): Flow<Unit> = flowOf { users.child(id).remove() }
    
}