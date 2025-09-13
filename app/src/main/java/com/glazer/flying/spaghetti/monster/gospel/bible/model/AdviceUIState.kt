package com.glazer.flying.spaghetti.monster.gospel.bible.model

import androidx.compose.runtime.Immutable

@Immutable
data class AdviceUIState(
    val isTextVisible: Boolean,
    val advice: String,
    val isAdLoading: Boolean,
    val isAdLoaded: Boolean,
    val showDialog: Boolean,
    val adviceCount: Int,
    val buttonState: ButtonUiState
) {

    companion object {

        val Initial = AdviceUIState(
            isTextVisible = true,
            advice = "",
            isAdLoading = false,
            isAdLoaded = false,
            showDialog = false,
            adviceCount = 0,
            buttonState = ButtonUiState.Advice
        )
    }
}