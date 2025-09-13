package com.glazer.flying.spaghetti.monster.gospel.bible.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.ColorSpaghetti

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValueSelector(
    label: String,
    startValue: String,
    listValues: List<String>,
    onValueChange: (String) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isDropdownExpanded,
        onExpandedChange = { isDropdownExpanded = it }
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val focusManager = LocalFocusManager.current

        LaunchedEffect(isDropdownExpanded) {
            if (!isDropdownExpanded) {
                focusManager.clearFocus(true)
            }
        }

        OutlinedTextField(
            value = startValue,
            onValueChange = { },
            label = {
                Text(
                    text = label,
                    fontSize = 16.sp
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
                focusedTextColor = MaterialTheme.colorScheme.onSecondary
            ),
            shape = RoundedCornerShape(8.dp),
            readOnly = true,
            interactionSource = interactionSource,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
            },
            textStyle = TextStyle.Default.copy(fontSize = 20.sp),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = {
                isDropdownExpanded = false
                focusManager.clearFocus(true)
            }, shape = RoundedCornerShape(8.dp),
            containerColor = ColorSpaghetti.copy(alpha = 0.5f)
        ) {
            listValues.forEach { value ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = value,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    },
                    onClick = {
                        if (value != startValue) {
                            onValueChange(value)
                        }
                        isDropdownExpanded = false
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }
}