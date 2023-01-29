package com.kekadoc.project.capybara.server.common.extensions

const val SYMBOL_DOT = '.'

private const val EMPTY_STRING = ""

internal fun emptyString(): String = EMPTY_STRING

internal fun String.getFileExtension(): String {
    return substringAfterLast(SYMBOL_DOT, emptyString())
}