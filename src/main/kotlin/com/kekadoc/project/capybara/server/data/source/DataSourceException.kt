package com.kekadoc.project.capybara.server.data.source

import com.kekadoc.project.capybara.server.data.model.Identifier
import java.lang.RuntimeException

sealed class DataSourceException(
    message: String?,
    cause: Throwable?,
) : RuntimeException(message, cause) {

    class EntityNotFound(message: String?, cause: Throwable?) : DataSourceException(message, cause) {
        companion object {

            fun byId(
                id: Identifier,
                name: String = "Entity",
                cause: Throwable? = null,
            ): EntityNotFound = byProps(
                name = name,
                cause = cause,
                properties = mapOf("id" to id),
            )

            fun byProps(
                name: String = "Entity",
                properties: Map<String, Any>,
                cause: Throwable? = null,
            ): EntityNotFound = EntityNotFound(
                message = "$name by $properties not found",
                cause = cause,
            )

        }
    }

}