package com.kekadoc.project.capybara.server.common.extensions

import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
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
                        continuation.resume(
                            snapshot.children.associate {
                                it.key to it.getValue(T::class.java)
                            }
                        )
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        continuation.resumeWithException(e)
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

suspend inline fun <reified T> DatabaseReference.set(value: T) {
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

suspend inline fun DatabaseReference.remove() {
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

suspend inline fun <reified T> DatabaseReference.runTransaction(noinline block: suspend (T?) -> T?): T? {
    return suspendCancellableCoroutine { continuation ->
        try {
            runTransaction(object : Transaction.Handler {

                override fun doTransaction(currentData: MutableData?): Transaction.Result = runBlocking {
                    val result = block.invoke(currentData?.getValue(T::class.java))
                    currentData?.value = result
                    Transaction.success(currentData)
                }

                override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                    when {
                        error != null -> continuation.resumeWithException(error.toException())
                        else -> continuation.resume(currentData?.getValue(T::class.java))
                    }
                }

            })
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