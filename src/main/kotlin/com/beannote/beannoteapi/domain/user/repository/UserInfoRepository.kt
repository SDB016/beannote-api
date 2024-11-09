package com.beannote.beannoteapi.domain.user.repository

import com.beannote.beannoteapi.domain.user.model.UserInfo
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserInfoRepository : CoroutineCrudRepository<UserInfo, ObjectId> {
    suspend fun findByNickname(nickname: String): UserInfo?
}
