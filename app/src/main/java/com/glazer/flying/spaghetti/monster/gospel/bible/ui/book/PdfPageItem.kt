package com.glazer.flying.spaghetti.monster.gospel.bible.ui.book

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun PdfPageItem(
    modifier: Modifier = Modifier,
    pageIndex: Int,
    bitmap: Bitmap?
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
            // Номер страницы
            Text(
                text = "Page ${pageIndex + 1}",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .padding(4.dp),
                color = Color.White,
                fontSize = 12.sp
            )
        }

    } ?: run {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//                .background(Color.Red.copy(alpha = 0.2f)),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("Failed to load page")
//        }
    }
}