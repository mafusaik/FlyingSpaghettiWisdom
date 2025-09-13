package com.glazer.flying.spaghetti.monster.gospel.bible.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.adaptiveSp
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.Tertiary
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.White

@Composable
fun DialogButton(text: String, textScale: Float, onClick: () -> Unit) {
    var buttonScale by remember { mutableFloatStateOf(1f) }
    val buttonScaleAnimation by animateFloatAsState(
        targetValue = buttonScale,
        animationSpec = tween(durationMillis = 200)
    )

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Tertiary,
            contentColor = White
        ),
        modifier = Modifier
            .scale(buttonScaleAnimation)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        buttonScale = 0.9f
                        tryAwaitRelease()
                        buttonScale = 1f
                    }
                )
            }
    ) {
        Text(
            text = text,
            fontSize = 18.adaptiveSp(textScale),
            fontWeight = FontWeight.Bold
        )
    }
}