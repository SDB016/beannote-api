package com.beannote.beannoteapi.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val description: String,
)
