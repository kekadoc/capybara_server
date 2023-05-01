package com.kekadoc.project.capybara.server.data.source.api.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kekadoc.project.capybara.server.Server
import com.kekadoc.project.capybara.server.data.model.Authorization
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Token
import com.kekadoc.project.capybara.server.data.repository.auth.AccessTokenValidation
import com.kekadoc.project.capybara.server.data.repository.auth.RefreshTokenValidation
import com.kekadoc.project.capybara.server.secure.JWTConfig
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

class AuthorizationDataSourceImpl(
    private val config: JWTConfig,
) : AuthorizationDataSource {

    private val algorithm = Algorithm.HMAC256(config.secret)

    override suspend fun authorizeUser(userId: Identifier, login: String): Authorization {
        val currentTimeMilliseconds = Server.getTime()
        return Authorization(
            accessToken = generateAccessToken(
                userId = userId,
                login = login,
                currentTimeMilliseconds = currentTimeMilliseconds,
            ),
            refreshToken = generateRefreshToken(
                userId = userId,
                login = login,
                currentTimeMilliseconds = currentTimeMilliseconds,
            ),
        )
    }

    override suspend fun fetchUser(accessToken: Token): AccessTokenValidation = try {
        val decoded = JWT.decode(accessToken)
        val currentDate = Date(Server.getTime())
        val expiresAt = decoded.expiresAt
        if (currentDate.after(expiresAt)) {
            AccessTokenValidation.Expired
        } else {
            val userId = UUID.fromString(decoded.subject)
            val login = decoded.getClaim("login").asString().orEmpty()
            AccessTokenValidation.Valid(
                userId = userId,
                login = login,
            )
        }
    } catch (e: Throwable) {
        AccessTokenValidation.Invalid(error = e)
    }

    override suspend fun validateRefreshToken(refreshToken: String): RefreshTokenValidation {
        return try {
            val decoded = JWT.decode(refreshToken)
            val currentDate = Date(Server.getTime())
            val expiresAt = decoded.expiresAt
            if (currentDate.after(expiresAt)) {
                RefreshTokenValidation.Expired
            } else {
                val userId = UUID.fromString(decoded.subject)
                val login = decoded.getClaim("login").asString()
                RefreshTokenValidation.Valid(
                    userId = userId,
                    login = login,
                )
            }
        } catch (e: Throwable) {
            RefreshTokenValidation.Invalid(error = e)
        }
    }

    private fun generateAccessToken(
        userId: Identifier,
        login: String,
        currentTimeMilliseconds: Long,
    ): String = JWT.create()
        .withSubject(userId.toString())
        .withExpiresAt(Date(currentTimeMilliseconds.withOffset(config.accessLifetimeHours.hours)))
        .withIssuer(config.issuer)
        .withAudience(config.audience)
        .withClaim("login", login)
        .sign(algorithm)

    private fun generateRefreshToken(
        userId: Identifier,
        login: String,
        currentTimeMilliseconds: Long,
    ): String = JWT.create()
        .withSubject(userId.toString())
        .withExpiresAt(Date(currentTimeMilliseconds.withOffset(config.refreshLifetimeHours.hours)))
        .withIssuer(config.issuer)
        .withAudience(config.audience)
        .withClaim("login", login)
        .sign(algorithm)

    private fun Long.withOffset(offset: Duration) = this + offset.inWholeMilliseconds

}