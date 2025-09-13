package com.glazer.flying.spaghetti.monster.gospel.bible.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.glazer.flying.spaghetti.monster.gospel.bible.data.utils.PdfRendererManager
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.model.ImageContent
import kotlin.math.max
import kotlin.math.min

class PdfPagingSource(
    private val rendererManager: PdfRendererManager
) : PagingSource<Int, ImageContent>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageContent> {
        Log.i("PAGE_LOAD", "PdfPagingSource load")
        return try {
            val pageCount = rendererManager.getPageCount()

            val startPage = params.key ?: 0
            val endPage = min(startPage + params.loadSize, pageCount) - 1
            val pages = (startPage..endPage).mapNotNull { pageIndex ->
                ImageContent(rendererManager.getPage(pageIndex))
            }

            val nextKey = if (endPage < pageCount - 1) endPage + 1 else null
            val prevKey = if (startPage > 0) {
                val prevStart = max(0, startPage - params.loadSize)
                prevStart
            } else null

            LoadResult.Page(
                data = pages,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ImageContent>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val pageSize = 10
            (anchorPosition / pageSize) * pageSize
        }
    }
}