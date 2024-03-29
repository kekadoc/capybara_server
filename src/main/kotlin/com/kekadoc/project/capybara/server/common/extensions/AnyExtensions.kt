package com.kekadoc.project.capybara.server.common.extensions

fun <T> T?.requireNotNull(): T {
    return requireNotNull(this)
}
fun <T> T?.requireNotNull(lazyMessage: () -> Any): T {
    return requireNotNull(this, lazyMessage)
}