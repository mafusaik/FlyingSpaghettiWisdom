package com.glazer.flying.spaghetti.monster.gospel.bible.ads

import android.app.Activity

interface AdManager {

    suspend fun ensureConsent(activity: Activity): Boolean

    suspend fun loadRewardedAd(): Result<Unit>

    fun showRewardedAd(activity: Activity, onReward: () -> Unit, onAdClosed: () -> Unit)

}