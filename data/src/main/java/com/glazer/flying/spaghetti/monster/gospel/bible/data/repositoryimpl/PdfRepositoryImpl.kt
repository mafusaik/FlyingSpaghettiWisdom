package com.glazer.flying.spaghetti.monster.gospel.bible.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.glazer.flying.spaghetti.monster.gospel.bible.data.paging.PdfPagingSource
import com.glazer.flying.spaghetti.monster.gospel.bible.data.utils.PdfRendererManager
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.model.ImageContent
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.repository.PdfRepository
import javax.inject.Inject

internal class PdfRepositoryImpl @Inject constructor(
    private val rendererManager: PdfRendererManager
) : PdfRepository {

    override suspend fun initializePdf(assetName: String): Boolean {
       return rendererManager.initialize(assetName)
    }

    override fun getPageCount(): Int {
       return rendererManager.getPageCount()
    }

    override fun getPdfPagingSource(savedPage: Int): Pager<Int, ImageContent> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 5,
                initialLoadSize = savedPage,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PdfPagingSource(rendererManager)
            }
        )
    }

    override fun cleanup() {
        rendererManager.cleanup()
    }

}