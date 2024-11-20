package com.beannote.beannoteapi.exception.advice

import com.beannote.beannoteapi.common.model.ErrorResponse
import com.beannote.beannoteapi.exception.ErrorCode
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.crypto.BadPaddingException

@RestControllerAdvice
class ExceptionHandler {
    private val logger = KotlinLogging.logger { }

    @ExceptionHandler(BadPaddingException::class)
    fun handleBadPaddingException(ex: BadPaddingException): ResponseEntity<ErrorResponse> {
        logger.error(ex) { "복호화 에러 발생!" }

        val errorResponse =
            ErrorResponse(
                errorCode = ErrorCode.BAD_PADDING_ERROR.name,
                reason = ErrorCode.BAD_PADDING_ERROR.description,
            )
        return ResponseEntity(errorResponse, ErrorCode.BAD_PADDING_ERROR.status)
    }
}
