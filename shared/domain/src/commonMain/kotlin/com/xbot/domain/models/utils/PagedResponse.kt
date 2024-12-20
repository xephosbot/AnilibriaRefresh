package com.xbot.domain.models.utils

data class PagedResponse<T>(
    val items: List<T>,
    val total: Int,
)