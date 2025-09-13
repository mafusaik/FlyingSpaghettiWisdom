package com.glazer.flying.spaghetti.monster.gospel.bible.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.White

@Composable
fun PermissionDialog(
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    onDismiss: () -> Unit
) {
    val openSettings: () -> Unit = {
        openAppSettings(context, launcher)
        onDismiss()
    }

    val textScale = rememberScreenScaleFactor()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
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
                Text(
                    text = stringResource(R.string.permission_needed),
                    color = White,
                    fontSize = 22.adaptiveSp(textScale),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    stringResource(R.string.text_permission_notifications),
                    color = White,
                    fontSize = 20.adaptiveSp(textScale),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DialogButton(stringResource(R.string.cancel), textScale) {
                        onDismiss()
                    }

                    DialogButton(stringResource(R.string.go_settings), textScale) {
                        openSettings()
                    }
                }
            }

        }
    }
}

fun openAppSettings(
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    launcher.launch(intent)
}