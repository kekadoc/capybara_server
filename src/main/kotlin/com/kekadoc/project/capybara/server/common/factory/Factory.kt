package com.kekadoc.project.capybara.server.common.factory

sealed interface Factory<R> {

    interface Nothing<R> : Factory<R> {

        fun create(): R

    }

    interface Single<T, R> : Factory<R> {

        fun create(value: T): R

    }

    interface Twin<T1, T2, R> : Factory<R> {

        fun create(value1: T1, value2: T2): R

    }

    interface Triple<T1, T2, T3, R> : Factory<R> {

        fun create(value1: T1, value2: T2, value3: T3): R

    }

}