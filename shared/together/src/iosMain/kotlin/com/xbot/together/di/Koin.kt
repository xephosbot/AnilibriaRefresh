@file:Suppress("unused")

package com.xbot.together.di

import com.xbot.domain.repository.TitleRepository
import org.koin.mp.KoinPlatform

fun getTitleRepository(): TitleRepository = KoinPlatform.getKoin().get()