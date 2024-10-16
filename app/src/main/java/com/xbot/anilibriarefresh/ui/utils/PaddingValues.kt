package com.xbot.anilibriarefresh.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

@Immutable
class UnionPaddingValues(
    private val first: PaddingValues,
    private val second: PaddingValues
) : PaddingValues {
    override fun calculateBottomPadding(): Dp {
        return maxOf(first.calculateBottomPadding(), second.calculateBottomPadding())
    }

    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp {
        return maxOf(first.calculateLeftPadding(layoutDirection), second.calculateLeftPadding(layoutDirection))
    }

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp {
        return maxOf(first.calculateRightPadding(layoutDirection), second.calculateRightPadding(layoutDirection))
    }

    override fun calculateTopPadding(): Dp {
        return maxOf(first.calculateTopPadding(), second.calculateTopPadding())
    }

    override fun equals(other: Any?): Boolean {
        if (other !is UnionPaddingValues) return false
        return first == other.first && second == other.second
    }

    override fun hashCode() = first.hashCode() * 31 + second.hashCode()

    override fun toString() = "UnionPaddingValues(first=$first, second=$second)"
}

fun PaddingValues.union(paddingValues: PaddingValues): PaddingValues = UnionPaddingValues(this, paddingValues)