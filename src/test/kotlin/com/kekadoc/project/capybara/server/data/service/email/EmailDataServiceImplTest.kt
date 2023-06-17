package com.kekadoc.project.capybara.server.data.service.email

import com.kekadoc.project.capybara.server.data.source.api.notifications.email.EmailNotificationConfig
import org.junit.Test

class EmailDataServiceImplTest {

    private val emailDataServiceImpl = EmailDataServiceImpl(
        config = EmailNotificationConfig(
            tokenSecret = "123",
            hostName = "a1",
            smtpPort = 123,
            username = "asd",
            password = "afasf",
            fromEmail = "afsasf",
            subject = "asfa"
        )
    )

    @Test
    fun test() {
//        val html = emailDataServiceImpl.buildEmailWithTempPasswordAndLogin(
//            "Олег", "Сергеевич", "Login123", "Pass#123"
//        )
//        println(html)
    }

}