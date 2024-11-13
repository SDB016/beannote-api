package com.beannote.beannoteapi.domain.user.application

import com.beannote.beannoteapi.config.UseMongoDBTestContainer
import com.beannote.beannoteapi.domain.user.model.request.SignUpRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@UseMongoDBTestContainer
@ActiveProfiles(profiles = ["dev"])
class AuthFacadeIntegrationTest(
    private val authFacade: AuthFacade,
    private val userInfoService: UserInfoService,
    private val credentialUserInfoService: CredentialUserInfoService,
) : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    private val logger = KotlinLogging.logger { }

    init {
        describe("AuthFacade Integration tests") {
            context("회원가입 요청이 주어지면") {
                it("유저를 등록하고, 토큰을 생성해 반환한다.") {
                    val username = "testUsername"
                    val signUpRequest = SignUpRequest(username, "testPassword")
                    val signUpResponse = authFacade.signUp(signUpRequest)

                    val accessToken = signUpResponse.accessToken

                    signUpResponse.shouldNotBeNull()
                    accessToken.shouldNotBeNull()
                    accessToken.shouldNotBeBlank()

                    val savedUserInfo = userInfoService.findByNickname(username)
                    val savedCredentialUserInfo = credentialUserInfoService.findByUsername(username)

                    savedUserInfo.shouldNotBeNull()
                    savedCredentialUserInfo.shouldNotBeNull()
                    savedUserInfo.id shouldBeEqual savedCredentialUserInfo.uid
                    savedUserInfo.nickname shouldBeEqual username
                    savedCredentialUserInfo.username shouldBeEqual username
                }
            }
        }
    }
}
