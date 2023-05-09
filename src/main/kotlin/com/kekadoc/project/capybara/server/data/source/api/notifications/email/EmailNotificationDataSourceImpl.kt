package com.kekadoc.project.capybara.server.data.source.api.notifications.email

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kekadoc.project.capybara.server.domain.model.*
import io.ktor.utils.io.charsets.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import java.nio.charset.StandardCharsets
import java.util.*

class EmailNotificationDataSourceImpl(
    private val config: EmailNotificationConfig,
) : EmailNotificationDataSource {

    private val algorithm = Algorithm.HMAC256(config.tokenSecret)
    private val host = if (com.kekadoc.project.capybara.server.Application.isDebug) {
        "http://localhost:8080/api/v1/notifications/email/answer"
    } else {
        "https://capybara-server.onrender.com/api/v1/notifications/email/answer"
    }

    override suspend fun sentEmailNotification(
        message: Message,
        users: List<User>,
    ) {
        users.map { user ->
            val userEmail = user.communications.values
                .find { it.type == Communication.Type.Email }
                ?.let(Communication::value)
                ?: return@map null
            val notificationText = buildMessage(
                head = config.subject,
                title = message.content.title,
                message = message.content.text,
                action1 = message.actions?.action1?.let { action1Text ->
                    action1Text to createAnswerRequestUrl(
                        userId = user.id,
                        notificationId = message.id,
                        answer = action1Text,
                    )
                } ,
                action2 = message.actions?.action2?.let { action2Text ->
                    action2Text to createAnswerRequestUrl(
                        userId = user.id,
                        notificationId = message.id,
                        answer = action2Text,
                    )
                },
                action3 = message.actions?.action3?.let { action3Text ->
                    action3Text to createAnswerRequestUrl(
                        userId = user.id,
                        notificationId = message.id,
                        answer = action3Text,
                    )
                }
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
                addTo(userEmail)
            }
        }
            .filterNotNull()
            .forEach(HtmlEmail::send)
    }

    override suspend fun receiveEmailAnswer(
        answerToken: Token,
    ): EmailNotificationAnswer? = parseAnswerToken(answerToken)

    private fun createAnswerToken(
        userId: Identifier,
        notificationId: Identifier,
        answer: String,
    ): Token = JWT.create()
        .withClaim("userId", userId.toString())
        .withClaim("notificationId", notificationId.toString())
        .withClaim("answer", answer)
        .sign(algorithm)

    private fun createAnswerRequestUrl(
        userId: Identifier,
        notificationId: Identifier,
        answer: String,
    ): String {
        return host + "?at=${createAnswerToken(userId, notificationId, answer)}"
    }

    private fun parseAnswerToken(answerToken: Token): EmailNotificationAnswer? {
        val decoded = JWT.decode(answerToken)
        val userId: UUID? = decoded.getClaim("userId").asString()?.let(UUID::fromString)
        val notificationId: UUID? = decoded.getClaim("notificationId").asString()?.let(UUID::fromString)
        val answer: String? = decoded.getClaim("answer").asString()
        return if (userId != null && notificationId != null && answer != null) {
            EmailNotificationAnswer(
                userId = userId,
                messageId = notificationId,
                answer = answer,
            )
        } else {
            null
        }
    }


    private fun buildMessage(
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