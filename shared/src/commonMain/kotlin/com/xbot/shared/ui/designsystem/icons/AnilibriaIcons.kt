package com.xbot.shared.ui.designsystem.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.PlaylistPlay
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star

object AnilibriaIcons {
    object Outlined {
        val Heart get() = Icons.Outlined.FavoriteBorder
        val Star get() = Icons.Outlined.Star
        val Home get() = Icons.Outlined.Home
        val Person get() = Icons.Outlined.PersonOutline
        val Search get() = Icons.Outlined.Search
        val ChevronLeft get() = Icons.Outlined.ChevronLeft
        val ChevronRight get() = Icons.Outlined.ChevronRight
        val ArrowBack get() = Icons.AutoMirrored.Outlined.ArrowBack
        val PlayArrow get() = Icons.Outlined.PlayArrow
        val Check get() = Icons.Outlined.Check
        val Clear get() = Icons.Outlined.Clear
        val CheckList get() = Icons.Outlined.Checklist
        val PlayList get() = Icons.AutoMirrored.Outlined.PlaylistPlay
    }
    object Filled {
        val Heart get() = Icons.Filled.Favorite
        val Star get() = Icons.Filled.Star
        val Home get() = Icons.Filled.Home
        val Person get() = Icons.Filled.Person
        val Share get() = Icons.Filled.Share
    }
}