package com.beannote.beannoteapi.domain.user.model

import com.beannote.beannoteapi.domain.common.model.BaseDocument
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "credential_user_info")
data class CredentialUserInfo(
    @Id
    val id: ObjectId = ObjectId(),
    val uid: ObjectId,
    val username: String,
    val password: String,
) : BaseDocument()
