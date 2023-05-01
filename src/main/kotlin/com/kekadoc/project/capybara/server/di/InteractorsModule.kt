package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.intercator.addressees.AddresseesInteractor
import com.kekadoc.project.capybara.server.intercator.addressees.AddresseesInteractorImpl
import com.kekadoc.project.capybara.server.intercator.auth.AuthInteractor
import com.kekadoc.project.capybara.server.intercator.auth.AuthInteractorImpl
import com.kekadoc.project.capybara.server.intercator.contacts.ContactsInteractor
import com.kekadoc.project.capybara.server.intercator.contacts.ContactsInteractorImpl
import com.kekadoc.project.capybara.server.intercator.groups.GroupsInteractor
import com.kekadoc.project.capybara.server.intercator.groups.GroupsInteractorImpl
import com.kekadoc.project.capybara.server.intercator.mobile_push.MobilePushInteractor
import com.kekadoc.project.capybara.server.intercator.mobile_push.MobilePushInteractorImpl
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
            groupsRepository = get(),
            fetchUserByAccessTokenFunction = get(),
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
            publicContactsRepository = get(),
            fetchUserByAccessTokenFunction = get(),
        )
    }

    single<GroupsInteractor> {
        GroupsInteractorImpl(
            authorizationRepository = get(),
            usersRepository = get(),
            groupsRepository = get(),
            fetchUserByAccessTokenFunction = get(),
        )
    }

    single<NotificationsInteractor> {
        NotificationsInteractorImpl(
            userRepository = get(),
            messagesRepository = get(),
            fetchUserByAccessTokenFunction = get(),
            distributionRepository = get(),
        )
    }

    single<MobilePushInteractor> {
        MobilePushInteractorImpl(
            mobileNotificationsRepository = get(),
            fetchUserByAccessTokenFunction = get(),
        )
    }

    single<ProfileInteractor> {
        ProfileInteractorImpl(
            userRepository = get(),
            //mobileNotificationsRepository = get(),
            fetchUserByAccessTokenFunction = get(),
        )
    }

    single<ScheduleInteractor> {
        ScheduleInteractorImpl()
    }

}