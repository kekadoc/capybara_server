package com.kekadoc.project.capybara.server.data.preparation

import org.jetbrains.exposed.sql.transactions.transaction

object Test : DataPreparation {

    override suspend fun condition(): Boolean = false

    override suspend fun prepare(): Unit = transaction {
        //Test something
    }

}