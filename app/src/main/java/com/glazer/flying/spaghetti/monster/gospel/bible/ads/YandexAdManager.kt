package com.glazer.flying.spaghetti.monster.gospel.bible.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.glazer.flying.spaghetti.monster.gospel.bible.utils.Constants.YANDEX_AD_UNIT_ID
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.common.MobileAds
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class YandexAdManager @Inject constructor(
    @ApplicationContext private val context: Context
) : AdManager {

    private var rewardedAdLoader: RewardedAdLoader? = null
    private var rewardedAd: RewardedAd? = null
    private var initialized = false

    override  suspend fun ensureConsent(activity: Activity): Boolean {
        return suspendCancellableCoroutine { continuation ->
            if (initialized) {
                continuation.resume(true)
            }
            MobileAds.initialize(context) {
                rewardedAdLoader = RewardedAdLoader(context)
                initialized = true
                continuation.resume(true)
            }
        }
    }

    
    // Загрузить rewarded рекламу
    override suspend fun loadRewardedAd(): Result<Unit> {
        return suspendCancellableCoroutine { continuation ->
            rewardedAdLoader?.setAdLoadListener(object : RewardedAdLoadListener {
                override fun onAdLoaded(rewarded: RewardedAd) {
                    rewardedAd = rewarded
                    Log.i(ADS_TAG, "onAdLoaded")
                    continuation.resume(Result.success(Unit))
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    Log.i(ADS_TAG, "onAdFailedToLoad ${error.description}")
                    continuation.resume(Result.failure(Exception(error.description)))
                }
            })
            val adRequestConfiguration =
                AdRequestConfiguration.Builder(YANDEX_AD_UNIT_ID).build()
            rewardedAdLoader?.loadAd(adRequestConfiguration)
        }
    }

    override fun showRewardedAd(activity: Activity, onReward: () -> Unit, onAdClosed: () -> Unit) {
        rewardedAd?.show(activity)
        rewardedAd?.setAdEventListener(object : RewardedAdEventListener {
            override fun onAdShown() {
                Log.i(ADS_TAG, "onAdShown")
            }

            override fun onAdDismissed() {
                rewardedAd?.setAdEventListener(null)
                rewardedAd = null
                onAdClosed()
            }

            override fun onRewarded(reward: Reward) {
                Log.i(ADS_TAG, "onRewarded")
                onReward()
            }

            override fun onAdFailedToShow(adError: AdError) {
                rewardedAd?.setAdEventListener(null)
                rewardedAd = null
            }

            override fun onAdImpression(impressionData: ImpressionData?) {}

            override fun onAdClicked() {}
        })
    }

    companion object{
        private const val ADS_TAG = "ADS_TAG_YANDEX"
    }
}

