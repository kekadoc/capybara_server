package com.kekadoc.project.capybara.server.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.dsl.module

val firebaseModule = module {
    single<FirebaseDatabase> { FirebaseDatabase.getInstance() }
    single<FirebaseMessaging> { FirebaseMessaging.getInstance() }
}