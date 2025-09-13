package com.glazer.flying.spaghetti.monster.gospel.bible.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import android.util.LruCache
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.ASSET_NAME_ENG
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.ASSET_NAME_RUS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

class PdfRendererManager(private val context: Context) {
    private var renderer: PdfRenderer? = null
    private val memoryCache = LruCache<String, Bitmap>(30)

    suspend fun initialize(assetName: String) = withContext(Dispatchers.IO) {
       deleteWrongBook(assetName)

        val tempFileName = "pdf_${assetName.hashCode()}.pdf"
        val tempFile = File(context.cacheDir, tempFileName)

        if (!tempFile.exists() && tempFile.name != assetName) {
            tempFile.createNewFile()
            context.assets.open(assetName).use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        renderer = PdfRenderer(
            ParcelFileDescriptor.open(
                tempFile,
                ParcelFileDescriptor.MODE_READ_ONLY
            )
        )
        return@withContext true
    }

    suspend fun getPage(pageIndex: Int): Bitmap? = withContext(Dispatchers.IO) {
        val cacheKey = "page_$pageIndex"
        memoryCache.get(cacheKey)?.let { return@withContext it }

        Log.i("PAGE_LOAD", "page $pageIndex renderer $renderer")

        while (renderer == null) {
            delay(100)
            getPage(pageIndex)
        }

        try {
            renderer?.openPage(pageIndex)?.use { page ->
                val bitmap = Bitmap.createBitmap(
                    page.width,
                    page.height,
                    Bitmap.Config.ARGB_8888
                ).apply {
                    page.render(this, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                }

                memoryCache.put(cacheKey, bitmap)
                return@withContext bitmap
            }
        } catch (e: Exception) {
            return@withContext null
        }
        return@withContext null
    }

    fun getPageCount(): Int = renderer?.pageCount ?: 0

    fun cleanup() {
        renderer?.close()
        memoryCache.evictAll()
    }

    private fun deleteWrongBook(assetName: String) {
        val oldFileName = if (assetName == ASSET_NAME_ENG) {
            "pdf_${ASSET_NAME_RUS.hashCode()}.pdf"
        }else {
            "pdf_${ASSET_NAME_ENG.hashCode()}.pdf"
        }
        val oldTempFile = File(context.cacheDir, oldFileName)
        if (oldTempFile.exists()) {
            memoryCache.evictAll()
            oldTempFile.delete()
        }
    }
}