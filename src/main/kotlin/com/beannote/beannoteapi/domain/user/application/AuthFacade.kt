package com.beannote.beannoteapi.domain.user.application

import com.beannote.beannoteapi.config.database.TransactionManager
import com.beannote.beannoteapi.domain.user.model.CredentialUserInfo
import com.beannote.beannoteapi.domain.user.model.UserInfo
import com.beannote.beannoteapi.domain.user.model.request.SignUpRequest
import com.beannote.beannoteapi.domain.user.model.response.SignUpResponse
import com.beannote.beannoteapi.exception.ErrorCode
import com.beannote.beannoteapi.exception.InvalidRequestException
import org.springframework.stereotype.Service

@Service
class AuthFacade(
    private val jwtProvider: JwtProvider,
    private val userInfoService: UserInfoService,
    private val credentialUserInfoService: CredentialUserInfoService,
    private val txManager: TransactionManager
) {
    suspend fun signUp(request: SignUpRequest): SignUpResponse {
        if (credentialUserInfoService.existsByUsername(request.username)) {
            throw InvalidRequestException(ErrorCode.DUPLICATE_USERNAME_ERROR)
        }

        val userInfo =
            txManager.executeWrite {
                val userInfo = userInfoService.save(UserInfo(nickname = request.username))

                credentialUserInfoService.save(
                    CredentialUserInfo(
                        uid = userInfo.id,
                        username = request.username,
                        password = request.password, // TODO("비번 암호화")
                    ),
                )
                userInfo
            }

        val accessToken = jwtProvider.createToken(userInfo.id)
        return SignUpResponse(accessToken)
    }
}
