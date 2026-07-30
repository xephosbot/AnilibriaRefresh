package com.xbot.shared

// Shared KMP umbrella module.
// Aggregates domain, data, network, di and state modules for every platform.
// It no longer produces an Apple framework on its own: the iOS binary is linked from
// :shared-ui, which pulls this module in transitively.

