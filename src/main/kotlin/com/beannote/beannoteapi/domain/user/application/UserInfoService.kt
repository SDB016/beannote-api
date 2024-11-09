package com.beannote.beannoteapi.domain.user.application

import com.beannote.beannoteapi.domain.user.model.UserInfo
import com.beannote.beannoteapi.domain.user.repository.UserInfoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserInfoService(
    private val userInfoRepository: UserInfoRepository
) {

    @Transactional
    suspend fun save(userInfo: UserInfo): UserInfo {
        return userInfoRepository.save(userInfo)
    }

    @Transactional
    suspend fun findByNickname(nickname: String): UserInfo? {
        return userInfoRepository.findByNickname(nickname)
    }
}
