package com.kekadoc.project.capybara.server

import com.kekadoc.project.capybara.server.common.component.Component
import com.kekadoc.project.capybara.server.di.Di
import com.kekadoc.project.capybara.server.routing.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import io.ktor.server.websocket.*
import org.koin.core.component.get
import java.time.Duration
import io.ktor.server.application.Application as KtorApplication

object Server : Component {
    
    fun getTime(): Long {
        return System.currentTimeMillis()
    }

    override fun init() {
        startServer()
    }

    private fun startServer() {
        val port = System.getenv(Config.PORT_KEY)?.toInt() ?: Config.DEFAULT_PORT_CODE
        val engine = Netty
        embeddedServer(factory = engine, port = port) {
            install(Resources)
            configureApplication()
            configureRouting()
        }.start(wait = true)
    }

    private fun KtorApplication.configureApplication() {
        install(ContentNegotiation) {
            json(json = Di.get())
        }
        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(15) // TODO: 14.09.2022 COnst
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }
    }

}