package com.kekadoc.project.capybara.server.common.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun <T> flowOf(block: suspend () -> T): Flow<T> {
    return flow { emit(block()) }
}

fun <T, R> Flow<T>.extract(block: T.() -> R): Flow<R> {
    return map { block.invoke(it) }
}