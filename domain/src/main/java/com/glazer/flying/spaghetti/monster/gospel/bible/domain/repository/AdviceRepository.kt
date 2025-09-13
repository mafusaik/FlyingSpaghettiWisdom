package com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository

import kotlinx.coroutines.flow.Flow


interface AdviceRepository {
    fun currentAdvice(): Flow<String>

    suspend fun restoreAdvices()

    suspend fun nextAdvice(): String

    suspend fun changeAdviceLang()

    fun setAdvice(advice: String)
}