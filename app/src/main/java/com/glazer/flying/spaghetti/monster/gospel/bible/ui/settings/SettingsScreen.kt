package com.glazer.flying.spaghetti.monster.gospel.bible.ui.settings

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.glazer.flying.spaghetti.monster.gospel.bible.R
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.langCodeToLang
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.langToLangCode
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.setAppLocale
import com.glazer.flying.spaghetti.monster.gospel.bible.model.SettingsEvent
import com.glazer.flying.spaghetti.monster.gospel.bible.model.SettingsUiState
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.components.CustomSwitch
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.components.NotificationTimeItem
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.components.PermissionDialog
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.components.ValueSelector
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.White
import kotlinx.coroutines.delay


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    insetsController: WindowInsetsControllerCompat
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val selectedLanguage by remember { mutableStateOf(uiState.selectedLanguage) }
    val startLanguage = selectedLanguage.langCodeToLang(context)

    val languages =
        listOf(stringResource(R.string.lang_english), stringResource(R.string.lang_russian))

    SettingsScreen(
        context = context,
        uiState = uiState,
        startLanguage = startLanguage,
        languages = languages,
        insetsController = insetsController,
        onEvent = { event -> viewModel.handleEvent(event) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    context: Context,
    uiState: SettingsUiState,
    startLanguage: String,
    languages: List<String>,
    insetsController: WindowInsetsControllerCompat,
    onEvent: (SettingsEvent) -> Unit
) {
    val launcherToSetting = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val isEnable = checkPermission(context)
        onEvent(SettingsEvent.EnableNotifications(isEnable, isEnable))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        onEvent(SettingsEvent.EnableNotifications(isGranted, isGranted))
    }

    LaunchedEffect(key1 = true) {
        delay(500)
        val isGranted = checkPermission(context)
        if (!isGranted) {
            onEvent(SettingsEvent.EnableNotifications(false, uiState.isNotificationEnabled))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    LaunchedEffect(true) {
        insetsController.isAppearanceLightStatusBars = false
    }

   // Log.d("RecompositionTracker", "SettingsScreen recomposition")
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Image(
            painter = painterResource(R.drawable.space_meatballs),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 74.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.title_settings),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            SettingsItem(
                stringResource(R.string.enable_notifications), uiState.isNotificationEnabled
            ) { isChecked ->
                onEvent(
                    SettingsEvent.EnableNotifications(
                        checkPermission(context), isChecked
                    )
                )
            }

            NotificationTimeItem(
                initialTime = uiState.notificationTime,
                onTimeSelected = {
                    onEvent(SettingsEvent.SaveNotificationTime(it.hour to it.minute))
                },
                uiState.isNotificationEnabled
            )

            ValueSelector(stringResource(R.string.language), startLanguage, languages) { language ->
                val languageCode = language.langToLangCode(context)
                context.setAppLocale(languageCode)
                onEvent(SettingsEvent.SelectLanguage(languageCode))
            }

            if (uiState.showPermissionDialog) {
                PermissionDialog(context, launcherToSetting, onDismiss = {
                    onEvent(SettingsEvent.ShowPermissionDialog(false))
                })
            }

            if (uiState.restartRequired) {
                (context as? Activity)?.recreate()
                onEvent(SettingsEvent.ResetRestartFlag)
            }
        }
    }
}

private fun checkPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else true
}

@Composable
fun SettingsItem(
    title: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 22.sp,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        CustomSwitch(isChecked) {
            onCheckedChange(it)
        }
    }
}