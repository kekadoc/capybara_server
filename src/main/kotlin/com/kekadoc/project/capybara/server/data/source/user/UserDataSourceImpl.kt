package com.kekadoc.project.capybara.server.data.source.user

import com.google.firebase.database.FirebaseDatabase
import com.kekadoc.project.capybara.server.common.extensions.*
import com.kekadoc.project.capybara.server.data.model.User
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

@OptIn(FlowPreview::class)
class UserDataSourceImpl : UserDataSource {
    
    private val database = FirebaseDatabase.getInstance()
    private val users = database.getReference("/users")

    
    override fun getUser(id: String): Flow<User?> {
        return flowOf { users.child(id).get<User>() }
    }
    
    override fun getUserByToken(token: String): Flow<User?> {
        return flowOf { users.orderByChild("authToken").equalTo(token).getAll<User>().values.firstOrNull() }
    }
    
    override fun getUserByLogin(login: String): Flow<User?> {
        return flowOf { users.orderByChild("profile/login").equalTo(login).getAll<User>().values.firstOrNull() }
    }
    
    override fun setUser(id: String, user: User): Flow<Unit> {
        return flowOf { users.child(id).set(user) }
    }
    
    override fun setUserByToken(token: String, user: User): Flow<Unit> {
        return getUserByToken(token).flatMapConcat { currentUser ->
            if (currentUser == null) throw RuntimeException("User by token not found")
            setUser(currentUser.profile.id, user)
        }
    }
    
    override fun deleteUser(id: String): Flow<Unit> {
        return flowOf { users.child(id).remove() }
    }
    
    override fun deleteUserByToken(token: String): Flow<Unit> {
        return getUserByToken(token).flatMapConcat { currentUser ->
            if (currentUser == null) throw RuntimeException("User by token not found")
            deleteUser(currentUser.profile.id)
        }
    }
    
}