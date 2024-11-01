package com.beannote.beannoteapi.domain.common.extension

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Instant.formatToSeoulTime(): String =
    DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .withZone(ZoneId.of("Asia/Seoul"))
        .format(this)
