@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.designsystem.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Immutable
private class UnionPaddingValues(private val first: PaddingValues, private val second: PaddingValues) :
    PaddingValues {
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

@Immutable
private class LimitPaddingValues(val padding: PaddingValues, val sides: WindowInsetsSides) :
    PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp {
        val layoutDirectionSide = if (layoutDirection == LayoutDirection.Ltr) {
            WindowInsetsSides.AllowLeftInLtr
        } else {
            WindowInsetsSides.AllowLeftInRtl
        }
        val allowLeft = sides.hasAny(layoutDirectionSide)
        return if (allowLeft) {
            padding.calculateLeftPadding(layoutDirection)
        } else {
            0.dp
        }
    }

    override fun calculateTopPadding(): Dp {
        return if (sides.hasAny(WindowInsetsSides.Top)) padding.calculateTopPadding() else 0.dp
    }

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp {
        val layoutDirectionSide = if (layoutDirection == LayoutDirection.Ltr) {
            WindowInsetsSides.AllowRightInLtr
        } else {
            WindowInsetsSides.AllowRightInRtl
        }
        val allowRight = sides.hasAny(layoutDirectionSide)
        return if (allowRight) {
            padding.calculateRightPadding(layoutDirection)
        } else {
            0.dp
        }
    }

    override fun calculateBottomPadding(): Dp {
        return if (sides.hasAny(WindowInsetsSides.Bottom)) padding.calculateBottomPadding() else 0.dp
    }

    override fun equals(other: Any?): Boolean {
        if (other !is LimitPaddingValues) return false
        return padding == other.padding && sides == other.sides
    }

    override fun hashCode(): Int {
        var result = padding.hashCode()
        result = 31 * result + sides.hashCode()
        return result
    }

    override fun toString(): String = "($padding only $sides)"
}

@Immutable
private class AddedPaddingValues(val first: PaddingValues, val second: PaddingValues) :
    PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp {
        return first.calculateLeftPadding(layoutDirection) +
                second.calculateLeftPadding(layoutDirection)
    }

    override fun calculateTopPadding(): Dp {
        return first.calculateTopPadding() + second.calculateTopPadding()
    }

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp {
        return first.calculateRightPadding(layoutDirection) +
                second.calculateRightPadding(layoutDirection)
    }

    override fun calculateBottomPadding(): Dp {
        return first.calculateBottomPadding() + second.calculateBottomPadding()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AddedPaddingValues) return false
        return first == other.first && second == other.second
    }

    override fun hashCode() = first.hashCode() * 31 + second.hashCode()

    override fun toString() = "($first + $second)"
}

@Immutable
private class SubtractedPaddingValues(val first: PaddingValues, val second: PaddingValues) :
    PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp {
        return (first.calculateLeftPadding(layoutDirection) -
                second.calculateLeftPadding(layoutDirection))
            .coerceAtLeast(0.dp)
    }

    override fun calculateTopPadding(): Dp {
        return (first.calculateTopPadding() - second.calculateTopPadding()).coerceAtLeast(0.dp)
    }

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp {
        return (first.calculateRightPadding(layoutDirection) -
                second.calculateRightPadding(layoutDirection))
            .coerceAtLeast(0.dp)
    }

    override fun calculateBottomPadding(): Dp {
        return (first.calculateBottomPadding() - second.calculateBottomPadding()).coerceAtLeast(
            0.dp
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SubtractedPaddingValues) return false
        return first == other.first && second == other.second
    }

    override fun hashCode() = first.hashCode() * 31 + second.hashCode()

    override fun toString() = "($first - $second)"
}

infix fun PaddingValues.union(paddingValues: PaddingValues): PaddingValues = UnionPaddingValues(this, paddingValues)
infix fun PaddingValues.only(sides: WindowInsetsSides): PaddingValues = LimitPaddingValues(this, sides)
infix fun PaddingValues.plus(other: PaddingValues): PaddingValues = AddedPaddingValues(this, other)
infix fun PaddingValues.minus(other: PaddingValues): PaddingValues = SubtractedPaddingValues(this, other)
