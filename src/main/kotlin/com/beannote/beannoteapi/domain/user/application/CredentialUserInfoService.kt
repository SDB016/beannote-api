package com.beannote.beannoteapi.domain.user.application

import com.beannote.beannoteapi.domain.user.model.CredentialUserInfo
import com.beannote.beannoteapi.domain.user.repository.CredentialUserInfoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CredentialUserInfoService(
    private val credentialUserInfoRepository: CredentialUserInfoRepository,
) {
    @Transactional(readOnly = true)
    suspend fun existsByUsername(username: String): Boolean = credentialUserInfoRepository.existsByUsername(username)

    @Transactional
    suspend fun save(credentialUserInfo: CredentialUserInfo): CredentialUserInfo {
        return credentialUserInfoRepository.save(credentialUserInfo)
    }

    @Transactional
    suspend fun findByUsername(username: String): CredentialUserInfo? {
        return credentialUserInfoRepository.findByUsername(username)
    }
}
