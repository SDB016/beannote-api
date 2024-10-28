package com.beannote.beannoteapi.exception.advice

import com.beannote.beannoteapi.common.model.ErrorResponse
import com.beannote.beannoteapi.exception.BeannoteException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class BeannoteExceptionHandler {
    private val logger = KotlinLogging.logger { }

    @ExceptionHandler(BeannoteException::class)
    protected fun handlerBeannoteException(e: BeannoteException): ResponseEntity<ErrorResponse> {
        logger.warn { "BeannoteException ${e.message}" }
        val response =
            ErrorResponse(
                errorCode = e.errorCode.name,
                reason = e.message ?: e.errorCode.description,
                extra = e.extra,
            )
        return ResponseEntity(response, e.errorCode.status)
    }
}
