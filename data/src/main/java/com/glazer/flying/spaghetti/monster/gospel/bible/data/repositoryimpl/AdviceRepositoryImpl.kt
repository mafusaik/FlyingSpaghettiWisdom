package com.glazer.flying.spaghetti.monster.gospel.bible.data.repositoryimpl

import android.content.Context
import android.content.res.Configuration
import com.glazer.flying.spaghetti.monster.gospel.bible.data.R
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.AdviceRepository
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.sharedpreferences.PrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

internal class AdviceRepositoryImpl @Inject constructor(
    newContext: Context,
    prefManager: PrefsManager
) : AdviceRepository {
    private var context: Context = newContext
    private var prefsManager = prefManager
    private val adviceSet = LinkedHashSet<String>()
    private val maxSize = 100

    private val currentAdvice = MutableStateFlow(prefsManager.recentAdvices.lastOrNull() ?: "")

    override fun currentAdvice(): Flow<String> {
        return currentAdvice.asStateFlow()
    }

    override suspend fun nextAdvice(): String {
        val advices = context.resources.getStringArray(R.array.advices)

        val recentAdvices = prefsManager.recentAdvices
        adviceSet.addAll(recentAdvices)

        var randomAdvice: String
        do {
            randomAdvice = advices[Random.nextInt(advices.size)]
        } while (recentAdvices.contains(randomAdvice))

        addAdvice(randomAdvice)

        return randomAdvice
    }

    private fun addAdvice(advice: String) {
        if (adviceSet.size >= maxSize) {
            val iterator = adviceSet.iterator()
            if (iterator.hasNext()) {
                iterator.next()
                iterator.remove()
            }
        }
        adviceSet.add(advice)
        prefsManager.recentAdvices = adviceSet
        setAdvice(advice)
    }

    override fun setAdvice(advice: String) {
        currentAdvice.value = advice
    }

    override suspend fun restoreAdvices() {
        val recentAdvices = prefsManager.recentAdvices
        if (recentAdvices.isNotEmpty()) adviceSet.addAll(recentAdvices)
        else adviceSet.add(nextAdvice())
    }


    override suspend fun changeAdviceLang() {
        val locale = when (prefsManager.currentLanguage) {
            "ru" -> Locale("ru")
            else -> Locale.ENGLISH
        }

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        context = context.createConfigurationContext(configuration)

        prefsManager.recentAdvices = emptySet()
        withContext(Dispatchers.IO){
            nextAdvice()
        }
    }
}