package com.beannote.beannoteapi.domain.user.application

import com.beannote.beannoteapi.common.encrypt.Encryptor
import com.beannote.beannoteapi.config.database.TransactionManager
import com.beannote.beannoteapi.domain.user.model.CredentialUserInfo
import com.beannote.beannoteapi.domain.user.model.UserInfo
import com.beannote.beannoteapi.domain.user.model.request.SignUpRequest
import com.beannote.beannoteapi.exception.ErrorCode
import com.beannote.beannoteapi.exception.InvalidRequestException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.springframework.transaction.ReactiveTransaction

class AuthFacadeTest :
    DescribeSpec({

        isolationMode = IsolationMode.InstancePerLeaf

        val credentialUserInfoService = mockk<CredentialUserInfoService>()
        val userInfoService = mockk<UserInfoService>()
        val txManager = mockk<TransactionManager>()
        val jwtProvider = mockk<JwtProvider>()
        val encryptor = mockk<Encryptor>().also { every { it.encrypt(any()) } returns "123123" }

        val authFacade =
            AuthFacade(
                credentialUserInfoService = credentialUserInfoService,
                userInfoService = userInfoService,
                txManager = txManager,
                jwtProvider = jwtProvider,
                encryptor = encryptor,
            )

        describe("SignUp") {
            val signUpRequest = SignUpRequest(username = "testUser", password = "password123")

            context("이미 사용자 이름이 존재하면") {
                it("DUPLICATE_USERNAME_ERROR를 에러코드로 담은 InvalidRequestException을 던진다") {
                    coEvery { credentialUserInfoService.existsByUsername(signUpRequest.username) } returns true

                    shouldThrow<InvalidRequestException> {
                        authFacade.signUp(signUpRequest)
                    }.also { ex ->
                        ex.errorCode shouldBe ErrorCode.DUPLICATE_USERNAME_ERROR
                    }

                    coVerify(exactly = 1) { credentialUserInfoService.existsByUsername(signUpRequest.username) }
                    confirmVerified(credentialUserInfoService)
                }
            }

            context("사용자 이름이 존재하지 않을 때") {
                context("트랜잭션이 성공적으로 실행되면") {
                    val savedUserInfo = UserInfo(id = ObjectId(), nickname = signUpRequest.username)
                    val accessToken = "mockAccessToken"
                    val txBlockSlot = slot<suspend (ReactiveTransaction) -> UserInfo>()

                    beforeEach {
                        coEvery { credentialUserInfoService.existsByUsername(signUpRequest.username) } returns false
                        coEvery { userInfoService.save(any()) } returns savedUserInfo
                        coEvery { credentialUserInfoService.save(any()) } returns
                            CredentialUserInfo(
                                uid = savedUserInfo.id,
                                username = signUpRequest.username,
                                password = "encryptedPassword",
                            )
                        coEvery { txManager.executeWrite(capture(txBlockSlot)) } answers {
                            val txBlock = txBlockSlot.captured
                            runBlocking { txBlock(mockk()) }
                            savedUserInfo
                        }
                        every { jwtProvider.createToken(savedUserInfo.id) } returns accessToken
                    }

                    it("사용자 정보와 자격 증명을 저장한다") {
                        authFacade.signUp(signUpRequest)

                        coVerify(exactly = 1) { userInfoService.save(any()) }
                        coVerify(exactly = 1) { credentialUserInfoService.save(any()) }
                    }

                    it("올바른 엑세스 토큰을 생성한다") {
                        val response = authFacade.signUp(signUpRequest)

                        response.accessToken shouldBe accessToken
                        verify(exactly = 1) { jwtProvider.createToken(savedUserInfo.id) }
                    }

                    it("트랜잭션이 성공적으로 실행된다") {
                        authFacade.signUp(signUpRequest)

                        coVerify(exactly = 1) { txManager.executeWrite(any()) }
                        confirmVerified(txManager)
                    }
                }
            }
        }
    })
