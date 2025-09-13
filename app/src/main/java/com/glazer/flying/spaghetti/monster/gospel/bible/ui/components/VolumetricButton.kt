package com.glazer.flying.spaghetti.monster.gospel.bible.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun VolumetricButton(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20),
    contentPadding: Dp = 20.dp,
    firstColor: Color = Color.White,
    secondColor: Color = Color.LightGray,
    contentAlignment: Alignment = Alignment.TopStart,
    onTouch: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    var touch by remember { mutableStateOf(false) }
    val shadowPadding by animateDpAsState(
        targetValue = if (touch) 4.dp else contentPadding,
        animationSpec = spring()
    )

    Box(
        modifier = modifier
            .background(Transparent, shape)
            .clickable{
                onTouch()
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.first()
                        touch = change.pressed
                      //  onTouch(change.pressed)
                    }
                }
            }
            .innerShadow(
                shape = shape,
                shadow = Shadow(
                    radius = shadowPadding,
                    color = firstColor,
                    offset = DpOffset(x = shadowPadding, y = shadowPadding)
                )
            )
            .innerShadow(
                shape = shape,
                shadow = Shadow(
                    radius = shadowPadding,
                    color = secondColor,
                    offset = DpOffset(x = -shadowPadding, y = -shadowPadding)
                )
            ),
        content = content,
        contentAlignment = contentAlignment
    )
}