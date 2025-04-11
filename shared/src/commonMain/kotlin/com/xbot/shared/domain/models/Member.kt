package com.xbot.shared.domain.models

import com.xbot.shared.domain.models.enums.MemberRole

data class Member(
    val id: String,
    val role: MemberRole?,
    val name: String,
    val avatar: Poster?,
)
