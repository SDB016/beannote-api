package com.beannote.beannoteapi.common.encrypt

data class Encryptor(
    private val key: String,
    private val algorithm: String,
) {
    fun encrypt(text: String): String {
        TODO("암호화 구현")
    }
}
