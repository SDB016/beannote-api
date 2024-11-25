package com.beannote.beannoteapi.domain.user.application

import com.beannote.beannoteapi.config.jwt.JwtConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import java.util.Date

private const val ACCESS_TOKEN = "access_token"

@Component
class JwtProvider(
    private val jwtProperties: JwtConfig.JwtProperties,
) {
    private val logger = KotlinLogging.logger {}

    init {
        require(jwtProperties.secret.toByteArray().size >= 32) {
            "JWT secret key must be at least 32 bytes long for security."
        }
    }

    private val key = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())

    fun createToken(id: ObjectId): String {
        val now = Date()
        val expirationDate = Date(now.time + jwtProperties.accessExp * 1000)

        return Jwts
            .builder()
            .subject(id.toString())
            .issuer(jwtProperties.issuer)
            .issuedAt(now)
            .audience()
            .add(jwtProperties.audience)
            .and()
            .expiration(expirationDate)
            .claim("type", ACCESS_TOKEN)
            .signWith(key)
            .compact()
    }

//    suspend fun validateToken(token: String): Principal? {}
}
