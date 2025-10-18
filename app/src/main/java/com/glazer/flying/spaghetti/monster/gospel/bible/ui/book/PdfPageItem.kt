package com.glazer.flying.spaghetti.monster.gospel.bible.ui.book

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.glazer.flying.spaghetti.monster.gospel.bible.R

@Composable
fun PdfPageItem(
    modifier: Modifier = Modifier,
    pageIndex: Int,
    bitmap: Bitmap?,
    isPageShow: Boolean
) {
    bitmap?.let { page ->
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = page,
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(page.width.toFloat() / page.height.toFloat())
                    .drawWithContent {
                        drawContent()
                    }
            )
            if (isPageShow) {
                Text(
                    text = stringResource(R.string.page_index, pageIndex + 1),
                    modifier = Modifier.align(Alignment.BottomCenter),
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

    } ?: run {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Red.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = stringResource(R.string.page_load_error),
                textAlign = TextAlign.Center
            )
        }
    }
}