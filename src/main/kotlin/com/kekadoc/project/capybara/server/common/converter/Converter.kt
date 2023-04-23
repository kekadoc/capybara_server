package com.kekadoc.project.capybara.server.common.converter

interface Converter<T, S> {

    fun convert(source: S): T

    interface Bidirectional<T, S> : Converter<T, S> {

        fun revert(target: T): S

    }

}