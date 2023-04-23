package com.kekadoc.project.capybara.server.common.secure

import com.kekadoc.project.capybara.server.common.extensions.emptyString

interface HashAlgorithm {

    fun hash(value: String, salt: String = emptyString(), ): String

}