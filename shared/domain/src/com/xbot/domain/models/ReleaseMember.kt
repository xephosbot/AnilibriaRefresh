package com.xbot.domain.models

import com.xbot.domain.models.enums.MemberRole

data class ReleaseMember(
    val id: String,
    val role: MemberRole?,
    val name: String,
    val avatar: Poster?,
)
