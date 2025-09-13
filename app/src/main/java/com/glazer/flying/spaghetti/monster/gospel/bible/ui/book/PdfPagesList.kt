package com.glazer.flying.spaghetti.monster.gospel.bible.ui.book

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.model.ImageContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@Composable
fun PdfPagesList(
    viewModel: BookViewModel,
    pagingData: Flow<PagingData<ImageContent>>
) {
    val pagingItems = pagingData.collectAsLazyPagingItems()
    val listState = rememberLazyListState()

    LaunchedEffect(true) {
        viewModel.getLastPage()
        delay(100)
        listState.scrollToItem(viewModel.scrollIndex.intValue)
        delay(100)
        listState.animateScrollBy(viewModel.scrollOffset.intValue.toFloat())
        Log.i("BOOK_STATE", "PdfPagesList LaunchedEffect")
    }

    DisposableEffect(Unit) {
        onDispose {
            val pageIndex = listState.firstVisibleItemIndex + 1
            val pageOffset = listState.firstVisibleItemScrollOffset
            Log.i("BOOK_STATE", "save index page $pageIndex offset $pageOffset")
            viewModel.saveLastPage(pageIndex, pageOffset)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            pagingItems.itemCount,
            key = { index -> pagingItems.peek(index)?.hashCode() ?: index }
        ) { pageIndex ->
            val imageContent = pagingItems[pageIndex]
            PdfPageItem(
                pageIndex = pageIndex,
                bitmap = imageContent?.data as Bitmap
            )
        }
    }
}