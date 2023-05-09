package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.domain.intercator.addressees.AddresseesInteractor
import com.kekadoc.project.capybara.server.domain.intercator.addressees.AddresseesInteractorImpl
import com.kekadoc.project.capybara.server.domain.intercator.auth.AuthInteractor
import com.kekadoc.project.capybara.server.domain.intercator.auth.AuthInteractorImpl
import com.kekadoc.project.capybara.server.domain.intercator.contacts.ContactsInteractor
import com.kekadoc.project.capybara.server.domain.intercator.contacts.ContactsInteractorImpl
import com.kekadoc.project.capybara.server.domain.intercator.email_message.EmailMessageInteractor
import com.kekadoc.project.capybara.server.domain.intercator.email_message.EmailMessageInteractorImpl
import com.kekadoc.project.capybara.server.domain.intercator.groups.GroupsInteractor
import com.kekadoc.project.capybara.server.domain.intercator.groups.GroupsInteractorImpl
import com.kekadoc.project.capybara.server.domain.intercator.mobile_push.MobilePushInteractor
import com.kekadoc.project.capybara.server.domain.intercator.mobile_push.MobilePushInteractorImpl
import com.kekadoc.project.capybara.server.domain.intercator.notification.MessagesInteractor
import com.kekadoc.project.capybara.server.domain.intercator.notification.MessagesInteractorImpl
import com.kekadoc.project.capybara.server.domain.intercator.profile.ProfileInteractor
import com.kekadoc.project.capybara.server.domain.intercator.profile.ProfileInteractorImpl
import com.kekadoc.project.capybara.server.domain.intercator.schedule.ScheduleInteractor
import com.kekadoc.project.capybara.server.domain.intercator.schedule.ScheduleInteractorImpl
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

    single<MessagesInteractor> {
        MessagesInteractorImpl(
            userRepository = get(),
            messagesRepository = get(),
            fetchUserByAccessTokenFunction = get(),
            messageWithNotificationManager = get(),
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
            fetchUserByAccessTokenFunction = get(),
        )
    }

    single<ScheduleInteractor> {
        ScheduleInteractorImpl()
    }

    single<EmailMessageInteractor> {
        EmailMessageInteractorImpl(
            emailNotificationRepository = get(),
            messagesRepository = get(),
        )
    }

}