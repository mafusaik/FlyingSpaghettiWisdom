package com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository

import androidx.paging.Pager
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.model.ImageContent


interface PdfRepository {
    suspend fun initializePdf(assetName: String): Boolean

    fun getPageCount(): Int

    fun getPdfPagingSource(savedPage: Int): Pager<Int, ImageContent>

    fun cleanup()
}