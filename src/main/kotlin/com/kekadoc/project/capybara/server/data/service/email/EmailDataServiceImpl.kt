package com.kekadoc.project.capybara.server.data.service.email

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kekadoc.project.capybara.server.Config
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.user.Communication
import com.kekadoc.project.capybara.server.domain.model.user.User
import com.kekadoc.project.capybara.server.domain.model.user.officialName
import com.kekadoc.project.capybara.server.domain.model.user.profile
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

    override suspend fun sentEmailInfoNotification(
        message: Message,
        author: User,
        users: List<User>,
    ) {
        users.map { user ->
            val userEmail = user.communications.values
                .find { it.type == Communication.Type.Email }
                ?.let(Communication::value)
                ?: return@map null
            val action1 = message.actions.getOrNull(0)
            val action2 = message.actions.getOrNull(1)
            val action3 = message.actions.getOrNull(2)

            val notificationText = MessageBuilder.buildInfoNotificationMessage(
                head = config.subject,
                title = message.title,
                message = message.text,
                author = author.profile.officialName,
                action1 = action1?.let { (id, action1Text) ->
                    val at = createAnswerToken(
                        userId = user.id,
                        notificationId = message.id,
                        answerId = id,
                    )
                    action1Text to RequestBuilder.createAnswerRequestUrl(at)
                } ,
                action2 = action2?.let { (id, action2Text) ->
                    val at = createAnswerToken(
                        userId = user.id,
                        notificationId = message.id,
                        answerId = id,
                    )
                    action2Text to RequestBuilder.createAnswerRequestUrl(at)
                },
                action3 = action3?.let { (id, action3Text) ->
                    val at = createAnswerToken(
                        userId = user.id,
                        notificationId = message.id,
                        answerId = id,
                    )
                    action3Text to RequestBuilder.createAnswerRequestUrl(at)
                }
            )
            createHtmlEmail(
                email = userEmail,
                text = notificationText,
                subject = "Информирование",
            )
        }
            .filterNotNull()
            .forEach(HtmlEmail::send)
    }

    override suspend fun sentEmailVoteNotification(
        message: Message,
        author: User,
        users: List<User>,
    ) {
        users.map { user ->
            val userEmail = user.communications.values
                .find { it.type == Communication.Type.Email }
                ?.let(Communication::value)
                ?: return@map null

            val notificationText = MessageBuilder.buildVoteNotificationMessage(
                head = config.subject,
                title = message.title,
                message = message.text,
                author = author.profile.officialName,
            )
            createHtmlEmail(
                email = userEmail,
                text = notificationText,
                subject = "Голосование",
            )
        }
            .filterNotNull()
            .forEach(HtmlEmail::send)
    }

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

    override suspend fun receiveEmailAnswer(
        answerToken: Token,
    ): EmailNotificationAnswer? = parseAnswerToken(answerToken)

    private fun parseAnswerToken(answerToken: Token): EmailNotificationAnswer? {
        val decoded = JWT.decode(answerToken)
        val userId: UUID? = decoded.getClaim("userId").asString()?.let(UUID::fromString)
        val notificationId: UUID? = decoded.getClaim("notificationId").asString()?.let(UUID::fromString)
        val answerId: Long? = decoded.getClaim("answerId").asLong()
        return if (userId != null && notificationId != null && answerId != null) {
            EmailNotificationAnswer(
                userId = userId,
                messageId = notificationId,
                answerId = answerId,
            )
        } else {
            null
        }
    }

    private fun createConfirmationToken(
        id: Identifier,
        email: String,
    ): Token = JWT.create()
        .withClaim("id", id.toString())
        .withClaim("email", email)
        .sign(algorithm)

    private fun createHtmlEmail(
        email: String,
        text: String,
        subject: String = config.subject,
    ): HtmlEmail = HtmlEmail().apply {
        println("_LOG_sentEMail $email $text")
        hostName = config.hostName
        setSmtpPort(465)
        setAuthenticator(DefaultAuthenticator(config.username, config.password))
        isSSLOnConnect = true
        setFrom(config.fromEmail)
        this.subject = subject
        setHtmlMsg(text)
        setCharset(StandardCharsets.UTF_8.name)
        addTo(email)
    }

    private fun sentEmail(
        email: String,
        text: String,
        subject: String = config.subject,
    ) {
        createHtmlEmail(
            email = email,
            text = text,
            subject = subject,
        ).send()
    }


    private fun createAnswerToken(
        userId: Identifier,
        notificationId: Identifier,
        answerId: Long,
    ): Token = JWT.create()
        .withClaim("userId", userId.toString())
        .withClaim("notificationId", notificationId.toString())
        .withClaim("answerId", answerId)
        .sign(algorithm)

}

private object RequestBuilder {

    fun createRegistrationConfirmEmailRequestUrl(
        token: Token,
    ): String = "${Config.publicApi}/api/v1/auth/registration/confirm_email?token=$token"

    fun createConfirmEmailRequestUrl(
        token: Token,
    ): String = "${Config.publicApi}/api/v1/profile/confirm/email?token=$token"

    fun createAnswerRequestUrl(token: String): String {
        return "${Config.publicApi}/api/v1/notifications/email/answer?at=$token"
    }

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

    fun buildInfoNotificationMessage(
        head: String,
        title: String?,
        message: String,
        author: String,
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
                if (title != null) { h4 { text(title) } }
                p { text(message) }
                if (action1 != null || action2 != null || action3 != null) {
                    table {
                        attributes["border"] = "0"
                        attributes["cellpadding"] = "0"
                        attributes["cellspacing"] = "0"
                        tr {
                            val actions = listOfNotNull(action1, action2, action3)
                            println("ACTIONS_$actions")
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
                br {  }
                p { text(author) }
            }
        }
    }

    fun buildVoteNotificationMessage(
        head: String,
        title: String?,
        message: String,
        author: String,
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
                if (title != null) { h4 { text(title) } }
                p { text(message) }
                br {  }
                p { text(author) }
                br {  }
                br {  }
                br {  }
                p { text("Проголосовать можно в мобильном приложении") }
                img(
                    alt = "Приложение",
                    src = "https://raw.githubusercontent.com/kekadoc/capybara_server/develop/src/main/resources/android_app_folder.png") {
                    attributes["width"] = "200"
                    attributes["height"] = "200"
                }
            }
        }
    }

}