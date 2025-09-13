package com.glazer.flying.spaghetti.monster.gospel.bible.extensions

import android.content.Context
import com.glazer.flying.spaghetti.monster.gospel.bible.R

fun String.langToLangCode(context: Context): String {
    return when(this){
        context.getString(R.string.lang_english) -> "en"
        else -> "ru"
    }
}

fun String.langCodeToLang(context: Context): String {
    return when(this){
        "en" -> context.getString(R.string.lang_english)
        else -> context.getString(R.string.lang_russian)
    }
}