package com.beannote.beannoteapi.exception

open class BeannoteException(
    val errorCode: ErrorCode,
    override val message: String? = errorCode.description,
    val extra: Map<String, Any>? = null,
) : RuntimeException(message)

class InvalidRequestException(
    errorCode: ErrorCode,
    message: String? = null,
) : BeannoteException(errorCode, message)
