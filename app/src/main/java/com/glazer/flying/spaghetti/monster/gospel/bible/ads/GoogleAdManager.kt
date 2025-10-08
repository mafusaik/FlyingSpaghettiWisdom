package com.glazer.flying.spaghetti.monster.gospel.bible.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.glazer.flying.spaghetti.monster.gospel.bible.utils.Constants
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class GoogleAdManager @Inject constructor(
    @ApplicationContext private val context: Context
) : AdManager {
    private var rewardedAd: RewardedAd? = null
    private var initialized = false

    override suspend fun ensureConsent(activity: Activity): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val consentManager = GoogleMobileAdsConsentManager.getInstance(context)

            consentManager.gatherConsent(activity) { consentError ->
                if (consentError != null) {
                    Log.w(ADS_TAG, "Consent error: ${consentError.errorCode}")
                }

                if (consentManager.canRequestAds) {
                    if (!initialized) {
                        MobileAds.initialize(context) {
                            initialized = true
                            continuation.resume(true)
                        }
                    } else {
                        continuation.resume(true)
                    }
                } else {
                    continuation.resume(false)
                }
            }
        }
    }

    override suspend fun loadRewardedAd(): Result<Unit> {
        return suspendCancellableCoroutine { continuation ->
            RewardedAd.load(context, Constants.AD_UNIT_ID, AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        rewardedAd = ad
                        continuation.resume(Result.success(Unit))
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.i(ADS_TAG, "onAdFailedToLoad ${error.message}")
                        continuation.resume(Result.failure(Exception(error.message)))
                    }
                })
        }
    }

    override fun showRewardedAd(activity: Activity, onReward: () -> Unit, onAdClosed: () -> Unit) {
        Log.i(ADS_TAG, "showRewardedAd $rewardedAd")
        rewardedAd?.show(activity) { onReward() }
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                onAdClosed()
            }
        }
    }

    companion object{
        private const val ADS_TAG = "ADS_TAG_GOOGLE"
    }
}