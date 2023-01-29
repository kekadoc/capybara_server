package com.kekadoc.project.capybara.server

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.kekadoc.project.capybara.server.di.dataSourceModule
import com.kekadoc.project.capybara.server.di.firebaseModule
import com.kekadoc.project.capybara.server.di.repositoryModule
import com.kekadoc.project.capybara.server.di.scheduleModule
import com.kekadoc.project.capybara.server.routing.configureRouting
import com.kekadoc.project.capybara.server.routing.configureSockets
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.websocket.*
import org.koin.core.context.startKoin
import java.io.FileInputStream
import java.time.Duration

object App

fun main() {
    initDi()
    initFirebase()
    startServer()
}


private fun initDi() {
    startKoin {
        modules(
            firebaseModule,
            repositoryModule,
            dataSourceModule,
            scheduleModule
        )
    }
}

private fun initFirebase() {

    val serviceAccount = App::class.java.getResourceAsStream("/file_01.json")
    
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://capybara-21-default-rtdb.europe-west1.firebasedatabase.app/")
        .build()
    
    FirebaseApp.initializeApp(options)
}

private fun startServer() {
    val port = System.getenv(Config.PORT_KEY)?.toInt() ?: Config.DEFAULT_PORT_CODE
    val engine = Netty
    embeddedServer(factory = engine, port = port) {
        configureApplication()
        configureRouting()
        //configureSockets()
    }.start(wait = true)
}

private fun Application.configureApplication() {
    install(ContentNegotiation) {
        json()
    }
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15) // TODO: 14.09.2022 COnst
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}
