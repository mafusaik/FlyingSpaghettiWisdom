package com.glazer.flying.spaghetti.monster.gospel.bible.extensions

import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Int.adaptiveSp(scale: Float): TextUnit {
    return (this * scale).sp
}

@Composable
fun rememberScreenScaleFactor(): Float {
    val screenWidthDp = rememberScreenWidthDp()

    return remember(screenWidthDp) {
        Log.i("SCREEN_SCALE", "Calculating scale for: $screenWidthDp")
        when {
            screenWidthDp < 370.dp -> 0.8f
            screenWidthDp < 480.dp -> 1.0f
            else -> 1.2f
        }
    }
}

@Composable
fun rememberScreenWidthDp(): Dp {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    return remember {
        derivedStateOf {
            with(density) { windowInfo.containerSize.width.toDp() }
        }
    }.value
}