package com.kekadoc.project.capybara.server.data.preparation

interface DataPreparation {

    suspend fun condition(): Boolean = true

    suspend fun prepare()

}