package com.glazer.flying.spaghetti.monster.gospel.bible.model

import android.app.Activity
import androidx.compose.runtime.Immutable

@Immutable
sealed class AdviceEvent {
    @Immutable
    data object GetAdvice : AdviceEvent()
    @Immutable
    data class CopyAdvice(val text: String) : AdviceEvent()
    @Immutable
    data class ShowAd(val activity: Activity?) : AdviceEvent()
    @Immutable
    data class ShowAdDialog(val isShow: Boolean, val isAdEnable: Boolean) : AdviceEvent()
}