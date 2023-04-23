package com.kekadoc.project.capybara.server.common.secure

import java.security.MessageDigest

object Hash : HashAlgorithm {

    override fun hash(value: String, salt: String): String {
        val algorithm = MessageDigest.getInstance("MD5")
        algorithm.update(salt.encodeToByteArray())
        algorithm.update(value.encodeToByteArray())
        return algorithm.digest().let(::String)
    }

}