package com.beannote.beannoteapi.domain.user.repository

import com.beannote.beannoteapi.domain.user.model.CredentialUserInfo
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CredentialUserInfoRepository : CoroutineCrudRepository<CredentialUserInfo, ObjectId> {
    suspend fun existsByUsername(username: String): Boolean
    suspend fun findByUsername(username: String): CredentialUserInfo?
}
