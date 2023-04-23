package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.intercator.addressees.AddresseesInteractor
import com.kekadoc.project.capybara.server.intercator.addressees.AddresseesInteractorImpl
import com.kekadoc.project.capybara.server.intercator.auth.AuthInteractor
import com.kekadoc.project.capybara.server.intercator.auth.AuthInteractorImpl
import com.kekadoc.project.capybara.server.intercator.contacts.ContactsInteractor
import com.kekadoc.project.capybara.server.intercator.contacts.ContactsInteractorImpl
import com.kekadoc.project.capybara.server.intercator.groups.GroupsInteractor
import com.kekadoc.project.capybara.server.intercator.groups.GroupsInteractorImpl
import com.kekadoc.project.capybara.server.intercator.notification.NotificationsInteractor
import com.kekadoc.project.capybara.server.intercator.notification.NotificationsInteractorImpl
import com.kekadoc.project.capybara.server.intercator.profile.ProfileInteractor
import com.kekadoc.project.capybara.server.intercator.profile.ProfileInteractorImpl
import com.kekadoc.project.capybara.server.intercator.schedule.ScheduleInteractor
import com.kekadoc.project.capybara.server.intercator.schedule.ScheduleInteractorImpl
import org.koin.dsl.module

val interactorsModule = module {

    single<AddresseesInteractor> {
        AddresseesInteractorImpl(
            userRepository = get(),
        )
    }

    single<AuthInteractor> {
        AuthInteractorImpl(
            authorizationRepository = get(),
            usersRepository = get(),
        )
    }

    single<ContactsInteractor> {
        ContactsInteractorImpl(
            userRepository = get(),
            contactsRepository = get(),
        )
    }

    single<GroupsInteractor> {
        GroupsInteractorImpl(
            usersRepository = get(),
            groupsRepository = get(),
        )
    }

    single<NotificationsInteractor> {
        NotificationsInteractorImpl(
            userRepository = get(),
            messagesRepository = get(),
        )
    }

    single<ProfileInteractor> {
        ProfileInteractorImpl(
            userRepository = get(),
            mobileNotificationsRepository = get(),
        )
    }

    single<ScheduleInteractor> {
        ScheduleInteractorImpl()
    }

}