package com.xbot.common.navigation

import androidx.navigation3.runtime.EntryProviderScope

typealias NavEntryBuilder = EntryProviderScope<NavKey>.(Navigator<NavKey>) -> Unit