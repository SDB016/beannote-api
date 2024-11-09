package com.beannote.beannoteapi.domain.common.model

import com.beannote.beannoteapi.domain.common.extension.formatToSeoulTime
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
@JsonIgnoreProperties(value = ["createdAt", "modifiedAt"], allowGetters = true)
abstract class BaseDocument {
    companion object {
        const val DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val TIMEZONE_SEOUL = "Asia/Seoul"
    }

    @CreatedDate
    private var _createdAt: Instant = Instant.now()

    @LastModifiedDate
    private var _modifiedAt: Instant = Instant.now()

    @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN, timezone = TIMEZONE_SEOUL)
    val createdAt: String
        get() = _createdAt.formatToSeoulTime()

    @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN, timezone = TIMEZONE_SEOUL)
    val modifiedAt: String
        get() = _modifiedAt.formatToSeoulTime()
}
