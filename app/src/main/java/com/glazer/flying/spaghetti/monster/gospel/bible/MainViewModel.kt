package com.glazer.flying.spaghetti.monster.gospel.bible

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.AdviceRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.workmanager.ResetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val resetRepository: ResetRepository,
    private val adviceRepository: AdviceRepository
) : ViewModel() {

    fun checkRecreate() {
        viewModelScope.launch {
            adviceRepository.changeAdviceLang()
        }
    }

    init {
        viewModelScope.launch {
           // resetRepository.cancelWork()

            resetRepository.isWorkDisable().collectLatest {
                Log.i("RESET_WORK", "${it}")
                if (!it) {
                    resetRepository.scheduleDailyResetWork()
                }
            }
        }
    }
}
