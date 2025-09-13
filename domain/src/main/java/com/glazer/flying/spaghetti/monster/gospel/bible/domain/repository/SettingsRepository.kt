package com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository


interface SettingsRepository {

    fun setIsRecreate(value: Boolean)

    fun getIsRecreate(): Boolean
}