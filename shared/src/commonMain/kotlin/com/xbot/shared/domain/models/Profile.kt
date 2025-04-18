package com.xbot.shared.domain.models

import kotlinx.datetime.LocalDateTime

data class Profile(
    val id: Int,
    val login: String,
    val email: String,
    val nickname: String?,
    val avatar: Poster?,
    val isBanned: Boolean,
    val createdAt: LocalDateTime,
)
