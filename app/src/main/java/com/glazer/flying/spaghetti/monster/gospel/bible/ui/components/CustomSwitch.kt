package com.glazer.flying.spaghetti.monster.gospel.bible.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@Stable
fun CustomSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = isChecked,
        onCheckedChange = {
            onCheckedChange(it)
        },
        colors = SwitchDefaults.colors(
            checkedTrackColor = MaterialTheme.colorScheme.primary,
            uncheckedTrackColor = MaterialTheme.colorScheme.tertiary,
            checkedThumbColor = MaterialTheme.colorScheme.secondary,
            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
            checkedBorderColor = MaterialTheme.colorScheme.primary,
            uncheckedBorderColor = MaterialTheme.colorScheme.tertiary,
            ),
        modifier = Modifier.padding(start = 8.dp),
        thumbContent = {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    )
            )
        }
    )
}