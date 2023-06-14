package com.kekadoc.project.capybara.server.data.service.email

import com.kekadoc.project.capybara.server.data.source.api.notifications.email.EmailNotificationConfig
import com.kekadoc.project.capybara.server.domain.model.Identifier
import io.ktor.utils.io.charsets.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import java.nio.charset.StandardCharsets

class EmailDataServiceImpl(
    private val config: EmailNotificationConfig,
) : EmailDataService {

    private val host = if (com.kekadoc.project.capybara.server.Application.isDebug) {
        "http://localhost:8080/api/v1/auth/registration"
    } else {
        "https://capybara-server.onrender.com/api/v1/auth/registration"
    }

    override suspend fun sentEmailWithLoginEndTempPassword(
        email: String,
        name: String,
        patronymic: String,
        login: String,
        password: String,
    ) {
        val notificationText = buildEmailWithTempPasswordAndLogin(
            name = name,
            patronymic = patronymic,
            login = login,
            password = password,
        )
        HtmlEmail().apply {
            hostName = config.hostName
            setSmtpPort(465)
            setAuthenticator(DefaultAuthenticator(config.username, config.password))
            isSSLOnConnect = true
            setFrom(config.fromEmail)
            subject = config.subject
            setHtmlMsg(notificationText)
            setCharset(StandardCharsets.UTF_8.name)
            addTo(email)
        }.send()
    }

    private fun buildEmailWithTempPasswordAndLogin(
        name: String,
        patronymic: String,
        login: String,
        password: String,
    ): String = buildString {
        append("<!DOCTYPE html>\n")
        appendHTML().html {
            lang = "ru"
            head {
                meta {
                    httpEquiv = "Content-Type"
                    content = "text/html"
                    charset = "utf-8"
                }
            }
            body {
                attributes["style"] = "font-family: Arial, sans-serif; font-size: 14px;"
                p {
                    text("Здравствуйте, $name $patronymic!")
                }
                br {
                    text("Вы зарегестрованы в системе ЧФ ПНИПУ")
                }
                br {
                    text("Для авторизации в системе используйте предоставленные логин и пароль:")
                }
                br {
                    text("Логин: $login")
                }
                br {
                    text("Пароль: $password")
                }
            }
        }
    }

    override suspend fun sentConfirmEmail(
        registrationId: Identifier,
        email: String,
    ) {
        val notificationText = buildConfirmEmailMessage(
            head = config.subject,
            title = "Подтверждение почты",
            message = "Нажмите что бы подтвердить почту",
            action1 = "Подтвердить" to createConfirmRequestUrl(registrationId),
        )
        HtmlEmail().apply {
            hostName = config.hostName
            setSmtpPort(465)
            setAuthenticator(DefaultAuthenticator(config.username, config.password))
            isSSLOnConnect = true
            setFrom(config.fromEmail)
            subject = config.subject
            setHtmlMsg(notificationText)
            setCharset(StandardCharsets.UTF_8.name)
            addTo(email)
        }.send()
    }

    private fun createConfirmRequestUrl(
        registrationId: Identifier,
    ): String {
        return "$host/$registrationId/confirm_email"
    }

    private fun buildConfirmEmailMessage(
        head: String,
        title: String?,
        message: String,
        action1: Pair<String, String>? = null,
        action2: Pair<String, String>? = null,
        action3: Pair<String, String>? = null,
    ): String = buildString {
        append("<!DOCTYPE html>\n")
        appendHTML().html {
            lang = "ru"
            head {
                meta {
                    httpEquiv = "Content-Type"
                    content = "text/html"
                    charset = "utf-8"
                }
                title(head)
            }
            body {
                attributes["style"] = "font-family: Arial, sans-serif; font-size: 14px;"
                if (title != null) {
                    p {
                        text(title)
                    }
                }
                p {
                    text(message)
                }
                if (action1 != null || action2 != null || action3 != null) {
                    table {
                        attributes["border"] = "0"
                        attributes["cellpadding"] = "0"
                        attributes["cellspacing"] = "0"
                        tr {
                            val actions = listOfNotNull(action1, action2, action3)
                            actions.forEachIndexed { index, (text, url) ->
                                td {
                                    attributes["align"] = "center"
                                    attributes["bgcolor"] = "#008CBA"
                                    attributes["style"] = "padding: 10px;"
                                    a(href = url) {
                                        this.attributes["style"] = "color:#ffffff;text-decoration:none;padding:10px 20px;border-radius:3px;"
                                        this.text(text)
                                    }
                                }
                                if (index < actions.size - 1) {
                                    td {
                                        attributes["width"] = "20"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}