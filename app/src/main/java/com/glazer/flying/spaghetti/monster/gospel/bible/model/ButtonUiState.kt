package com.glazer.flying.spaghetti.monster.gospel.bible.model


sealed class ButtonUiState {
        data object Loading : ButtonUiState()
        data object Advice : ButtonUiState()
        data object Offering : ButtonUiState()
        data object IsOver : ButtonUiState()
    }