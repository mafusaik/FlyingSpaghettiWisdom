package com.glazer.flying.spaghetti.monster.gospel.bible.ui.book

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowInsetsControllerCompat
import com.glazer.flying.spaghetti.monster.gospel.bible.R
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.rememberScreenWidthDp
import com.glazer.flying.spaghetti.monster.gospel.bible.model.PdfUiState
import kotlinx.coroutines.async

@Composable
fun BookScreen(
    viewModel: BookViewModel,
    insetsController: WindowInsetsControllerCompat
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        insetsController.isAppearanceLightStatusBars = true
    }

    LaunchedEffect(Unit) {
        val isInit = async {
            viewModel.initPgfBook()
        }.await()
        if (isInit) viewModel.getPgfData()
    }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val screenWidth = rememberScreenWidthDp()

    val gestureModifier = Modifier.pointerInput(Unit) {
        detectTransformGestures(
            onGesture = { _, pan, zoom, _ ->
                val newScale = scale * zoom

                if (newScale >= 1f) {
                    scale = newScale.coerceIn(1f, 5f)

                    val maxOffset = (screenWidth.toPx() * (scale - 1f)) / 2f
                    offset = Offset(
                        x = (offset.x + pan.x).coerceIn(-maxOffset, maxOffset),
                        y = (offset.y + pan.y).coerceIn(-maxOffset, maxOffset)
                    )
                } else {
                    scale = 1f
                    offset = Offset.Zero
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(gestureModifier)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }
    ) {
        when (val state = uiState) {
            is PdfUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is PdfUiState.Success -> {
                PdfPagesList(viewModel, state.data)
            }

            is PdfUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val textError = state.message ?: run { stringResource(R.string.unknown_error) }
                    Text(stringResource(R.string.error, textError))
                }
            }
        }
    }
}