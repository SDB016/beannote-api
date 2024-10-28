package com.beannote.beannoteapi.common.model

data class ErrorResponse(
    val errorCode: String,
    val reason: String,
    val extra: Map<String, Any>? = null,
)
