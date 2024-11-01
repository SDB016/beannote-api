package com.beannote.beannoteapi.domain.user.repository

import com.beannote.beannoteapi.domain.user.model.CredentialUserInfo
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface CredentialUserInfoRepository : CoroutineCrudRepository<CredentialUserInfo, ObjectId> {
    fun existsByUsername(username: String): Mono<Boolean>
}
