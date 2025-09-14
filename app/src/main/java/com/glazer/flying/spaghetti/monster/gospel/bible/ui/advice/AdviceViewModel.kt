package com.glazer.flying.spaghetti.monster.gospel.bible.ui.advice

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glazer.flying.spaghetti.monster.gospel.bible.model.AdviceUIState
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.AdviceRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.PreferencesRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.SystemClipboard
import com.glazer.flying.spaghetti.monster.gospel.bible.model.AdviceEvent
import com.glazer.flying.spaghetti.monster.gospel.bible.model.ButtonUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdviceViewModel @Inject constructor(
    private val adviceRepository: AdviceRepository,
    private val prefRepository: PreferencesRepository,
    private val clipboard: SystemClipboard
) : ViewModel() {

    private var isDialogShowed = false

    private val _uiState = MutableStateFlow(AdviceUIState.Initial)
    val state: StateFlow<AdviceUIState> =
        combine(
            _uiState,
            adviceRepository.currentAdvice().distinctUntilChanged(),
            prefRepository.getAdviceAmountFlow().distinctUntilChanged()
        ) { state, advice, amount ->
            isDialogShowed = prefRepository.getIsDialogShowed()
            state.copy(
                advice = advice,
                adviceCount = amount,
                buttonState = getButtonState(amount, isDialogShowed)
            )
        }
            .filter { it != AdviceUIState.Initial }
            .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            AdviceUIState.Initial
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            adviceRepository.restoreAdvices()
        }
    }

    fun handleEvent(event: AdviceEvent?) {
        if (event == null) return
        when (event) {
            is AdviceEvent.GetAdvice -> {
                Log.i("ADVICE_TAG", "GetAdvice")
                getAdvice()
                incrementAdviceCount()
            }

            is AdviceEvent.CopyAdvice -> {
                Log.i("ADVICE_TAG", "CopyAdvice")
                copyAdvice(event.text)
            }

            is AdviceEvent.ShowAdDialog -> {
                Log.i("ADVICE_TAG", "ShowAdDialog ${event.isShow}")
                val buttonState = if (event.isAdEnable) ButtonUiState.Offering
                else ButtonUiState.IsOver
                _uiState.update { it.copy(showDialog = event.isShow, buttonState = buttonState) }
                if (!isDialogShowed) {
                    isDialogShowed = true
                    prefRepository.setIsDialogShowed(true)
                }
            }
        }
    }

    private fun incrementAdviceCount() {
        val newAmount = state.value.adviceCount + 1
        Log.i("ADVICE_TAG", "incrementAdviceCount $newAmount")
        updateCount(newAmount)
    }

    private fun decrementAdviceCount() {
        val newAmount = state.value.adviceCount - 1
        Log.i("ADVICE_TAG", "decrementAdviceCount $newAmount")
        updateCount(newAmount)
    }

    private fun updateCount(newAmount: Int) {
        val buttonState = getButtonState(newAmount, isDialogShowed)
        _uiState.update { it.copy(adviceCount = newAmount, buttonState = buttonState) }
        prefRepository.setAdviceAmount(newAmount)
    }

    private fun getButtonState(amount: Int, isDialogShowed: Boolean): ButtonUiState {
        return if (amount < 3) ButtonUiState.Advice
        else if (isDialogShowed) ButtonUiState.Offering
        else ButtonUiState.IsOver
    }

    private fun getAdvice() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isTextVisible = false) }
            delay(300)

            adviceRepository.nextAdvice()
            _uiState.update { it.copy(isTextVisible = true) }
        }
    }

    private fun copyAdvice(text: String) {
        clipboard.copyToClipboard(text)
    }

    fun setInitAdvice(advice: String) {
        adviceRepository.setAdvice(advice)
    }
}