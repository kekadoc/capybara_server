package com.kekadoc.project.capybara.server.common.http

import io.ktor.http.*

sealed class Header(
    private val identity: Identity<*>
) {

    val name: String
        get() = identity.name
    
    data class Authorization(val value: String) : Header(Key) {
        companion object Key : Identity<Authorization>("Authorization") {
            override fun newInstance(value: String): Authorization {
                return Authorization(value)
            }
        }
    }
    
    data class ApiKey(val value: String) : Header(Key) {
        companion object Key : Identity<ApiKey>("ApiKey") {
            override fun newInstance(value: String): ApiKey {
                return ApiKey(value)
            }
        }
    }
    
    abstract class Identity<T : Header>(
        val name: String
    ) {
        abstract fun newInstance(value: String): T
    }
    
    companion object {
        
        private val identities = setOf(
            Authorization,
            ApiKey
        )
        
        fun parse(headers: io.ktor.http.Headers): Set<Header> {
            return headers.names()
                .mapNotNull { name -> identities.find { it.name.contentEquals(other = name, ignoreCase = true) } }
                .mapNotNull { identity -> headers[identity.name]?.let { identity.newInstance(it) } }
                .toSet()
        }
    }
    
}

fun <T : Header> Headers.get(identity: Header.Identity<T>): T? {
    return this[identity.name]?.let(identity::newInstance)
}