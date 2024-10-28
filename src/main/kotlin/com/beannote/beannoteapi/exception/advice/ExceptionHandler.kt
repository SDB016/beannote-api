package com.beannote.beannoteapi.exception.advice

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    private val logger = KotlinLogging.logger { }
}
