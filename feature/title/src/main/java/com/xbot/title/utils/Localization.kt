package com.xbot.title.utils

import androidx.annotation.StringRes
import com.xbot.domain.models.enums.MemberRole
import com.xbot.title.R

@get:StringRes
val MemberRole.stringRes: Int
    get() = when (this) {
        MemberRole.POSTER -> R.string.member_role_poster
        MemberRole.TIMING -> R.string.member_role_timing
        MemberRole.VOICING -> R.string.member_role_voicing
        MemberRole.EDITING -> R.string.member_role_editing
        MemberRole.DECORATING -> R.string.member_role_decorating
        MemberRole.TRANSLATING -> R.string.member_role_translating
    }