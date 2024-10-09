package com.xbot.domain.model

data class TitleModel(
    val id: Int,
    val name: String,
    val description: String,
    val posterUrl: String?,
    val uploadedTime: Long
)
