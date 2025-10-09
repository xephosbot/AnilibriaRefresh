package com.xbot.common.navigation

import androidx.navigation3.runtime.EntryProviderBuilder

typealias NavEntryBuilder = EntryProviderBuilder<NavKey>.(Navigator<NavKey>) -> Unit