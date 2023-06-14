package com.kekadoc.project.capybara.server.data.source.api.notifications.email

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kekadoc.project.capybara.server.Config
import com.kekadoc.project.capybara.server.domain.model.Communication
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.User
import com.kekadoc.project.capybara.server.domain.model.message.Message
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

    override suspend fun sentEmailNotification(
        message: Message,
        users: List<User>,
    ) {
        users.map { user ->
            val userEmail = user.communications.values
                .find { it.type == Communication.Type.Email }
                ?.let(Communication::value)
                ?: return@map null
            val action1 = message.actions.getOrNull(0)
            val action2 = message.actions.getOrNull(2)
            val action3 = message.actions.getOrNull(3)

            val notificationText = buildMessage(
                head = config.subject,
                title = message.title,
                message = message.text,
                action1 = action1?.let { (id, action1Text) ->
                    action1Text to createAnswerRequestUrl(
                        userId = user.id,
                        notificationId = message.id,
                        answerId = id,
                    )
                } ,
                action2 = action2?.let { (id, action2Text) ->
                    action2Text to createAnswerRequestUrl(
                        userId = user.id,
                        notificationId = message.id,
                        answerId = id,
                    )
                },
                action3 = action3?.let { (id, action3Text) ->
                    action3Text to createAnswerRequestUrl(
                        userId = user.id,
                        notificationId = message.id,
                        answerId = id,
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
        answerId: Long,
    ): Token = JWT.create()
        .withClaim("userId", userId.toString())
        .withClaim("notificationId", notificationId.toString())
        .withClaim("answerId", answerId)
        .sign(algorithm)

    private fun createAnswerRequestUrl(
        userId: Identifier,
        notificationId: Identifier,
        answerId: Long,
    ): String {
        return "${Config.publicApi}/api/v1/notifications/email/answer?at=${createAnswerToken(userId, notificationId, answerId)}"
    }

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