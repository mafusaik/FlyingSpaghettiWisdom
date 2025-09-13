package com.glazer.flying.spaghetti.monster.gospel.bible.data.repositoryimpl

import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.SettingsRepository

class SettingsRepositoryImpl : SettingsRepository {

    private var isRecreate = false

    override fun setIsRecreate(value: Boolean) {
        isRecreate = value
    }

    override fun getIsRecreate(): Boolean {
        return isRecreate
    }
}