package com.glazer.flying.spaghetti.monster.gospel.bible.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.glazer.flying.spaghetti.monster.gospel.bible.R
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.adaptiveSp
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.rememberScreenScaleFactor
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.ColorMeatball
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.ColorSpaghetti
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.Tertiary
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.White

@Composable
fun OfferingDialog(
    onDonate: () -> Unit,
    onDismiss: () -> Unit
) {
    val textScale = rememberScreenScaleFactor()
    val maxWidth = if(textScale > 1) 0.7f
    else 0.9f

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(maxWidth)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(ColorMeatball, ColorSpaghetti)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_pirate),
                        contentDescription = null,
                        tint = Tertiary,
                        modifier = Modifier.size(60.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(R.string.dialog_stop_message),
                        color = White,
                        fontSize = 20.adaptiveSp(textScale),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.offset(y = (-2).dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .background(ColorMeatball, CircleShape)
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.dialog_message_limit),
                        color = White,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Icon(
                    painter = painterResource(R.drawable.fish),
                    contentDescription = null,
                    tint = Tertiary,
                    modifier = Modifier
                        .size(64.dp)
                        .rotate(90f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .background(ColorSpaghetti, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.dialog_message_offering),
                        color = Tertiary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DialogButton(stringResource(R.string.cancel), textScale) {
                        onDismiss()
                    }

                    DialogButton(stringResource(R.string.ok), textScale) {
                        onDonate()
                    }
                }
            }
        }
    }
}



