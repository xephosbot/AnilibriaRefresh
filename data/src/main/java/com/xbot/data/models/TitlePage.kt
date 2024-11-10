package com.xbot.data.models

import com.xbot.domain.models.TitleModel

data class TitlePage(
    val items: List<TitleModel>,
    val total: Int,
)
