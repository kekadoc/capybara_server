package com.kekadoc.project.capybara.server.routing.api.system

import com.kekadoc.project.capybara.server.common.PipelineContext
import com.kekadoc.project.capybara.server.common.authToken
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.domain.intercator.system.SystemInteractor
import com.kekadoc.project.capybara.server.routing.util.execute
import com.kekadoc.project.capybara.server.routing.util.execution.get
import com.kekadoc.project.capybara.server.routing.verifier.ApiKeyVerifier
import com.kekadoc.project.capybara.server.routing.verifier.AuthorizationVerifier
import io.ktor.server.routing.*
import org.koin.core.component.get

fun Route.system() = route("/system") {

    get("/features") { getSystemFeatures() }

}

private suspend fun PipelineContext.getSystemFeatures(

) = execute(
    ApiKeyVerifier,
    AuthorizationVerifier
) {
    Di.get<SystemInteractor>().getSystemFeatures(
        accessToken = authToken,
    )
}
