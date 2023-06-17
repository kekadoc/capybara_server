package com.kekadoc.project.capybara.server.data.service.email

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kekadoc.project.capybara.server.Config
import com.kekadoc.project.capybara.server.data.source.api.notifications.email.EmailNotificationConfig
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.user.User
import io.ktor.utils.io.charsets.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import java.nio.charset.StandardCharsets
import java.util.*

class EmailDataServiceImpl(
    private val config: EmailNotificationConfig,
) : EmailDataService {

    private val algorithm = Algorithm.HMAC256(config.tokenSecret)

    override suspend fun sentEmailWithLoginEndTempPassword(
        email: String,
        name: String,
        patronymic: String?,
        login: String,
        password: String,
    ) {
        val notificationText = MessageBuilder.buildEmailWithTempPasswordAndLogin(
            name = name,
            patronymic = patronymic,
            login = login,
            password = password,
        )
        sentEmail(
            email = email,
            text = notificationText,
        )
    }

    override suspend fun sentConfirmEmail(
        registrationId: Identifier,
        email: String,
    ) {
        val confirmToken = createConfirmationToken(registrationId, email)
        val confirmRequest = RequestBuilder.createRegistrationConfirmEmailRequestUrl(confirmToken)
        val text = MessageBuilder.buildConfirmEmailMessage(
            head = "Подтверждение почты",
            title = "Подтверждение почты для оформления заявки на регистрацию в системе ЧФ ПНИПУ",
            message = buildString {
                append("Ваша заявка на регистрацию успешно принята.")
                append("Для продолжения необходимо подтвердить e-mail.")
                append("После подтверждения администрация сможет рассмотреть вашу заявку.")
            },
            action1 = "Подтвердить" to confirmRequest,
        )
        sentEmail(
            email = email,
            subject = "Подтверждение почты",
            text = text,
        )
    }

    override suspend fun sentConfirmEmail(user: User, email: String) {
        val confirmToken = createConfirmationToken(user.id, email)
        val confirmRequest = RequestBuilder.createConfirmEmailRequestUrl(confirmToken)
        val text = MessageBuilder.buildConfirmEmailMessage(
            head = "Подтверждение почты",
            title = "Подтверждение почты для системы оповещения ЧФ ПНИПУ",
            message = "После подтверждения на нее сможет приходить полезная информация.",
            action1 = "Подтвердить" to confirmRequest,
        )
        sentEmail(
            email = email,
            subject = "Подтверждение почты",
            text = text,
        )
    }

    override suspend fun checkEmailConfirmation(
        token: Token,
    ): EmailDataService.EmailConfirmation? = try {
        val decoded = JWT.decode(token)
        val id = UUID.fromString(decoded.getClaim("id").asString())
        val email = decoded.getClaim("email").asString()
        EmailDataService.EmailConfirmation(
            id = id,
            email = email,
        )
    } catch (e: Throwable) {
        null
    }


    private fun createConfirmationToken(
        id: Identifier,
        email: String,
    ): Token = JWT.create()
        .withClaim("id", id.toString())
        .withClaim("email", email)
        .sign(algorithm)

    private fun sentEmail(
        email: String,
        text: String,
        subject: String = config.subject,
    ) {
        println("_LOG_sentEMail $email $text")
        HtmlEmail().apply {
            hostName = config.hostName
            setSmtpPort(465)
            setAuthenticator(DefaultAuthenticator(config.username, config.password))
            isSSLOnConnect = true
            setFrom(config.fromEmail)
            this.subject = subject
            setHtmlMsg(text)
            setCharset(StandardCharsets.UTF_8.name)
            addTo(email)
        }.send()
    }

}

private object RequestBuilder {

    fun createRegistrationConfirmEmailRequestUrl(
        token: Token,
    ): String = "${Config.publicApi}/api/v1/auth/registration/confirm_email?token=$token"

    fun createConfirmEmailRequestUrl(
        token: Token,
    ): String = "${Config.publicApi}/api/v1/profile/confirm/email?token=$token"

}

private object MessageBuilder {

    fun buildEmailWithTempPasswordAndLogin(
        name: String,
        patronymic: String?,
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
                    if (patronymic != null) {
                        text("Здравствуйте, $name $patronymic!")
                    } else {
                        text("Здравствуйте, $name!")
                    }
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

    fun buildConfirmEmailMessage(
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