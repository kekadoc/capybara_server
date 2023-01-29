package com.kekadoc.project.capybara.server.common.extensions

import java.io.File
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal suspend fun createTempFileSuspend(prefix: String, suffix: String): File {
    return suspendCoroutine { continuation: Continuation<File> ->
        runCatching {
            File.createTempFile(prefix, suffix)
        }.onFailure {
            continuation.resumeWithException(it)
        }.onSuccess {
            continuation.resume(it)
        }
    }
}