package com.kekadoc.project.capybara.server

import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

fun io.ktor.server.application.Application.initializer() {
    runBlocking { Application.init(this@initializer) }
}
