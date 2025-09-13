package com.glazer.flying.spaghetti.monster.gospel.bible.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glazer.flying.spaghetti.monster.gospel.bible.R
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTimeItem(
    initialTime: Pair<Int, Int>,
    onTimeSelected: (TimePickerState) -> Unit,
    isEnabled: Boolean = true,
) {
    var showTimePicker by remember { mutableStateOf(false) }

    val timeState = rememberTimePickerState(
        initialHour = initialTime.first,
        initialMinute = initialTime.second,
        is24Hour = true
    )

    val textColor = if (isEnabled) {
        White
    } else {
        White.copy(alpha = 0.5f)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.time_for_advice),
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterVertically)
        )

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.Transparent,
            tonalElevation = 2.dp,
            modifier = Modifier
                .wrapContentWidth()
                .clickable(
                    enabled = isEnabled,
                    onClick = { showTimePicker = true },
                )
                .border(
                    width = 1.dp,
                    color = if (isEnabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    },
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                text = stringResource(R.string.time_formatted, timeState.hour, timeState.minute),
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
        }
    }

    if (showTimePicker && isEnabled) {
        TimePickerField(
            initialTime = timeState,
            onConfirm = { time ->
                timeState.hour = time.hour
                timeState.minute = time.minute
                onTimeSelected(timeState)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}