package com.kekadoc.project.capybara.server.common.extensions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun Query.isExist(): Boolean {
    return suspendCancellableCoroutine { continuation ->
        addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                continuation.resume(snapshot.exists())
            }
            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }
}

suspend inline fun <reified T> Query.get(): T? {
    return suspendCancellableCoroutine { continuation ->
        addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    try {
                        continuation.resume(snapshot.getValue(T::class.java))
                    } catch (e: Throwable) {
                        continuation.resume(null)
                    }
                } else {
                    continuation.resume(null)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }
}

suspend inline fun <reified T> Query.getAll(): Map<String, T?> {
    return suspendCancellableCoroutine { continuation ->
        addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    try {
                        continuation.resume(snapshot.children.associate { it.key to it.getValue(T::class.java) })
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        continuation.resume(emptyMap())
                    }
                } else {
                    continuation.resume(emptyMap())
                }
            }
            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }
}

suspend inline fun Query.getKey(): String? {
    return suspendCancellableCoroutine { continuation ->
        addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    try {
                        continuation.resume(snapshot.key)
                    } catch (e: Throwable) {
                        continuation.resume(null)
                    }
                } else {
                    continuation.resume(null)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }
}

suspend inline fun <reified T> com.google.firebase.database.DatabaseReference.set(value: T) {
    return suspendCancellableCoroutine { continuation ->
        try {
            this.setValue(value) { error, _ ->
                if (error == null) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(error.toException())
                }
            }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }
}

suspend inline fun com.google.firebase.database.DatabaseReference.remove() {
    return suspendCancellableCoroutine { continuation ->
        try {
            this.removeValue { error, ref ->
                if (error == null) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(error.toException())
                }
            }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }
}


inline fun <reified T> Query.observeValue(): Flow<T?> {
    return callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    try {
                        val value = snapshot.getValue(T::class.java)
                        trySend(value)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        //trySend(null)
                    }
                } else {
                    trySend(null)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                val exception = error.toException()
                exception.printStackTrace()
                close(exception)
            }
        }
        addValueEventListener(listener)
        awaitClose {
            removeEventListener(listener)
        }
    }
}