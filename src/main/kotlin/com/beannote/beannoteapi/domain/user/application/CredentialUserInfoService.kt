package com.beannote.beannoteapi.domain.user.application

import com.beannote.beannoteapi.domain.user.model.CredentialUserInfo
import com.beannote.beannoteapi.domain.user.repository.CredentialUserInfoRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

@Service
class CredentialUserInfoService(
    private val credentialUserInfoRepository: CredentialUserInfoRepository,
) {
    suspend fun existsByUsername(username: String): Boolean = credentialUserInfoRepository.existsByUsername(username).awaitSingle()

    fun save(credentialUserInfo: CredentialUserInfo) {
        TODO("Not yet implemented")
    }
}
