/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.data.di

import com.xbot.data.datasource.FiltersDataSource
import com.xbot.data.datasource.TitleDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single { TitleDataSource(client = get()) }
    single { FiltersDataSource(client = get()) }
}
