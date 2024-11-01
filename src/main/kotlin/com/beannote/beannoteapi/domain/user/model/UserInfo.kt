package com.beannote.beannoteapi.domain.user.model

import com.beannote.beannoteapi.domain.common.model.BaseDocument
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user_info")
data class UserInfo(
    @Id
    val id: ObjectId = ObjectId(),
    val nickname: String,
    var email: String? = null,
) : BaseDocument()
