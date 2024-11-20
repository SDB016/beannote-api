package com.beannote.beannoteapi.config.encrypt

import com.beannote.beannoteapi.common.encrypt.Encryptor
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(EncryptConfig.EncryptProperties::class)
class EncryptConfig {
    private val logger = KotlinLogging.logger {}

    @ConfigurationProperties(prefix = "encrypt")
    data class EncryptProperties(
        val key: String,
        val algorithm: String,
    )

    @Bean
    fun encryptor(properties: EncryptProperties): Encryptor {
        logger.info { "Encryptor initialized with key: ${properties.key} and algorithm: ${properties.algorithm}" }
        return Encryptor(properties.key, properties.algorithm)
    }
}
