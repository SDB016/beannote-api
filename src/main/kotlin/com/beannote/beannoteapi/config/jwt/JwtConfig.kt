package com.beannote.beannoteapi.config.jwt

import com.beannote.beannoteapi.domain.user.application.JwtProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(JwtConfig.JwtProperties::class)
class JwtConfig(
    private val jwtProperties: JwtProperties,
) {
    private val logger = KotlinLogging.logger {}

    @ConfigurationProperties(prefix = "auth.jwt")
    data class JwtProperties(
        @field:NotBlank
        val secret: String = "",
        @field:NotBlank
        val accessExp: Int = 0,
        @field:NotBlank
        val refreshExp: Int = 0,
        val issuer: String = "your-api-name",
        val audience: String = "your-api-name",
        val tokenPrefix: String = "Bearer ",
    )

    @Bean
    fun jwtProvider(jwtProperties: JwtProperties): JwtProvider {
        logger.info { "initialized jwtProvider. $jwtProperties" }
        return JwtProvider(jwtProperties)
    }
}
