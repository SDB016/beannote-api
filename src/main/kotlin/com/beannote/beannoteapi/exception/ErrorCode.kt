package com.beannote.beannoteapi.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val description: String,
) {
    DUPLICATE_USERNAME_ERROR(HttpStatus.BAD_REQUEST, "중복된 아이디입니다."),
    BAD_PADDING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "변조된 암호화된 데이터가 감지되었습니다."),
}
