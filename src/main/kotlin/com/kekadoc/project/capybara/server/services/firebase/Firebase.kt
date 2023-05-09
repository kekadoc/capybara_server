package com.kekadoc.project.capybara.server.services.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.kekadoc.project.capybara.server.Application
import com.kekadoc.project.capybara.server.common.component.Component

object Firebase : Component {

    override fun init(application: io.ktor.server.application.Application) {
        val serviceAccount = Application::class.java.getResourceAsStream("/file_01.json")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://capybara-21-default-rtdb.europe-west1.firebasedatabase.app/")
            .build()

        FirebaseApp.initializeApp(options)
    }

}