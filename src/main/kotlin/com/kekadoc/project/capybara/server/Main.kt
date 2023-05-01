package com.kekadoc.project.capybara.server

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

fun io.ktor.server.application.Application.initializer() {
    Application.init(this)
}
