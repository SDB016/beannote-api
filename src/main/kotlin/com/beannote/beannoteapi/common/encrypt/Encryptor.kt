package com.beannote.beannoteapi.common.encrypt

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random
import kotlin.text.Charsets.UTF_8

class Encryptor(
    private val key: String,
    private val algorithm: String = "AES/CBC/PKCS5Padding",
) {
    private val keySpec: SecretKeySpec
    private val ivSize: Int

    init {
        val keyBytes = key.toByteArray(UTF_8)
        require(keyBytes.size in listOf(16, 24, 32)) {
            "Key must be 16, 24, or 32 bytes for AES"
        }
        keySpec = SecretKeySpec(keyBytes, algorithm.substringBefore("/"))
        ivSize =
            when (algorithm.substringBefore("/")) {
                "AES" -> 16
                else -> throw IllegalArgumentException("Unsupported algorithm: $algorithm")
            }
    }

    fun encrypt(plainText: String): String {
        val iv = generateIv()
        val cipher = createCipher(Cipher.ENCRYPT_MODE, iv)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(UTF_8))
        return (iv + encryptedBytes).encodeBase64()
    }

    fun decrypt(encryptedText: String): String {
        val decodedBytes = encryptedText.decodeBase64()
        require(decodedBytes.size > ivSize) {
            "Invalid encrypted text: size is less than IV size"
        }
        val (ivBytes, encryptedBytes) = decodedBytes.splitBytes(ivSize)
        val cipher = createCipher(Cipher.DECRYPT_MODE, ivBytes)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return decryptedBytes.toString(UTF_8)
    }

    private fun createCipher(
        mode: Int,
        iv: ByteArray,
    ): Cipher =
        Cipher.getInstance(algorithm).apply {
            init(mode, keySpec, IvParameterSpec(iv))
        }

    private fun generateIv(): ByteArray = Random.nextBytes(ivSize)

    private fun ByteArray.encodeBase64(): String = Base64.getEncoder().encodeToString(this)

    private fun String.decodeBase64(): ByteArray = Base64.getDecoder().decode(this)

    private fun ByteArray.splitBytes(ivSize: Int): Pair<ByteArray, ByteArray> {
        require(size > ivSize) { "Invalid input data: size is less than IV size" }
        return sliceArray(0 until ivSize) to sliceArray(ivSize until size)
    }
}
