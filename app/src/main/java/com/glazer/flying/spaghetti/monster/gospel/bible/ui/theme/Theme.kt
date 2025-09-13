package com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = ColorMeatball,
    secondary = ColorSpaghetti,
    tertiary = Tertiary

)

@Composable
fun PastafarianTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = customTypography,
        content = content
    )
}