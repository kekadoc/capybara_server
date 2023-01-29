package com.kekadoc.project.capybara.server.common.http

sealed class Header {
    
    class Authorization(val value: String) : Header() {
        companion object : Identity<Authorization>("Authorization") {
            override fun newInstance(value: String): Authorization {
                return Authorization(value)
            }
        }
    }
    
    class ApiKey(val value: String) : Header() {
        companion object : Identity<ApiKey>("ApiKey") {
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