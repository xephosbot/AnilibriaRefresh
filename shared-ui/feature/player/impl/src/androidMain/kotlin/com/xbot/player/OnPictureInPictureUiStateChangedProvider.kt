package com.xbot.player

import android.app.PictureInPictureUiState
import androidx.core.util.Consumer

interface OnPictureInPictureUiStateChangedProvider {
    fun addOnPictureInPictureUiStateChangedListener(
        listener: Consumer<PictureInPictureUiState>
    )

    fun removeOnPictureInPictureUiStateChangedListener(
        listener: Consumer<PictureInPictureUiState>
    )
}