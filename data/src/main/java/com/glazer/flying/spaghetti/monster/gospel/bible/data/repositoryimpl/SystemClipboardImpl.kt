package com.glazer.flying.spaghetti.monster.gospel.bible.data.repositoryimpl

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.SystemClipboard
import javax.inject.Inject

internal class SystemClipboardImpl @Inject constructor(
    private val context: Context
) : SystemClipboard {
    override fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }
}