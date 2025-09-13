package com.glazer.flying.spaghetti.monster.gospel.bible.ui.book

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.ASSET_NAME_ENG
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.ASSET_NAME_RUS
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.DEFAULT_LANGUAGE
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.PdfRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.PreferencesRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.model.PdfUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class BookViewModel @Inject constructor(
    private val pdfRepository: PdfRepository,
    private val prefRepository: PreferencesRepository,
) : ViewModel() {

    var scrollIndex = mutableIntStateOf(0)
    var scrollOffset = mutableIntStateOf(0)

    private val _uiState = MutableStateFlow<PdfUiState>(PdfUiState.Loading)
    val uiState = _uiState.asStateFlow()


    suspend fun initPgfBook() = withContext(Dispatchers.IO) {
        Log.i("BOOK_STATE", "init")
        val language = prefRepository.getCurrentLanguage()
        val assetName = if (language == DEFAULT_LANGUAGE) ASSET_NAME_ENG
        else ASSET_NAME_RUS

        _uiState.value = PdfUiState.Loading
        return@withContext pdfRepository.initializePdf(assetName)
    }

    fun getPgfData() {
        viewModelScope.launch {
            try {
                val pageCount = pdfRepository.getPageCount()
                val savedPage = scrollIndex.intValue
                Log.i("BOOK_STATE", "getPgfData $savedPage")
                val flow = pdfRepository.getPdfPagingSource(savedPage).flow.cachedIn(viewModelScope)

                _uiState.update { PdfUiState.Success(pageCount, flow) }

            } catch (e: Exception) {
                _uiState.value = PdfUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun saveLastPage(index: Int, offset: Int) {
        if (scrollIndex.intValue != index || scrollOffset.intValue != offset) {
            scrollIndex.intValue = index
            scrollOffset.intValue = offset
            prefRepository.setSavedPage(index)
            prefRepository.setSavedOffset(offset)
        }
    }


    fun getLastPage() {
        val index = prefRepository.getSavedPage()
        val offset = prefRepository.getSavedOffset()
        scrollIndex.intValue = index
        scrollOffset.intValue = offset
    }

    override fun onCleared() {
        pdfRepository.cleanup()
        super.onCleared()
    }
}
