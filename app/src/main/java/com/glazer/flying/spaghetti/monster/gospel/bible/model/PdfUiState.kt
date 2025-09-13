package com.glazer.flying.spaghetti.monster.gospel.bible.model

import androidx.paging.PagingData
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.model.ImageContent
import kotlinx.coroutines.flow.Flow

sealed class PdfUiState {
        data object Loading : PdfUiState()
        data class Success(val pageCount: Int, val data: Flow<PagingData<ImageContent>>) : PdfUiState()
        data class Error(val message: String) : PdfUiState()
    }