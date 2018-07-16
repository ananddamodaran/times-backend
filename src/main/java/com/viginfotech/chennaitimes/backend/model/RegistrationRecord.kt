package com.viginfotech.chennaitimes.backend.model

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index


@Entity
class RegistrationRecord {

    @Id
    internal var id: Long? = null

    @Index
    var regId: String? = null
}
