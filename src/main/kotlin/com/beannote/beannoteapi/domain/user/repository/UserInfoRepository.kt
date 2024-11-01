package com.beannote.beannoteapi.domain.user.repository

import com.beannote.beannoteapi.domain.user.model.UserInfo
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserInfoRepository : CoroutineCrudRepository<UserInfo, ObjectId>
