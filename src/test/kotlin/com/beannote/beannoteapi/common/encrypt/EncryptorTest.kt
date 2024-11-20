package com.beannote.beannoteapi.common.encrypt

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.Base64

class EncryptorTest :
    DescribeSpec(
        {

            val key = "1234567890123456"
            val algorithm = "AES/CBC/PKCS5Padding"
            val encryptor = Encryptor(key, algorithm)

            describe("Encryptor") {
                context("암호화, 복호화 할 때") {
                    it("정확하게 암호화, 복호화 해야함") {
                        val plainText = "커피없인 못살어~"

                        val encryptedText = encryptor.encrypt(plainText)
                        val decryptedText = encryptor.decrypt(encryptedText)

                        decryptedText shouldBe plainText
                    }
                }

                context("잘못된 키 길이로 Encryptor를 초기화할 때") {
                    it("예외 발생") {
                        val invalidKey = "shortKey"

                        val exception =
                            shouldThrow<IllegalArgumentException> {
                                Encryptor(invalidKey, algorithm)
                            }
                        exception.message shouldBe "Key must be 16, 24, or 32 bytes for AES"
                    }
                }

                context("암호화된 텍스트가 변조된 경우") {
                    val plainText = "커피없인 못살어~"
                    val encryptedText = encryptor.encrypt(plainText)
                    val originalBytes = Base64.getDecoder().decode(encryptedText)

                    fun tamperBytes(
                        bytes: ByteArray,
                        range: IntRange,
                        tamperOperation: (Byte) -> Byte,
                    ): ByteArray =
                        bytes.copyOf().apply {
                            range.forEach { index ->
                                this[index] = tamperOperation(this[index])
                            }
                        }
                    context("패딩이 변조된 경우") {
                        it("복호화 시 BadPaddingException이 발생") {
                            val tamperedBytes =
                                tamperBytes(originalBytes, originalBytes.size - 16 until originalBytes.size) {
                                    (it + 1).toByte()
                                }
                            val tamperedText = tamperedBytes.encodeBase64()

                            shouldThrow<javax.crypto.BadPaddingException> {
                                encryptor.decrypt(tamperedText)
                            }
                        }
                    }

                    context("IV가 변조된 경우") {
                        it("복호화 결과가 원본과 달라야 함") {
                            val tamperedBytes =
                                tamperBytes(originalBytes, 0 until 16) {
                                    (it + 1).toByte()
                                }
                            val tamperedText = tamperedBytes.encodeBase64()

                            val decryptedText = encryptor.decrypt(tamperedText)
                            decryptedText shouldNotBe plainText
                        }
                    }
                }
            }
        },
    )

private fun ByteArray.encodeBase64(): String = Base64.getEncoder().encodeToString(this)
