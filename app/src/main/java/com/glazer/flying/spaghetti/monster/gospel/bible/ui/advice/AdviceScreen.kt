package com.glazer.flying.spaghetti.monster.gospel.bible.ui.advice

import android.app.Activity
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.glazer.flying.spaghetti.monster.gospel.bible.R
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.adaptiveSp
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.rememberScreenScaleFactor
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.showToast
import com.glazer.flying.spaghetti.monster.gospel.bible.model.AdviceEvent
import com.glazer.flying.spaghetti.monster.gospel.bible.model.AdviceUIState
import com.glazer.flying.spaghetti.monster.gospel.bible.model.ButtonUiState
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.components.VolumetricButton
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.components.OfferingDialog
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.ColorMeatball
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.ColorMeatballDark

@Composable
fun AdviceScreen(
    viewModel: AdviceViewModel,
    initialAdvice: String,
    insetsController: WindowInsetsControllerCompat
) {
    val activity = LocalActivity.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val textScale = rememberScreenScaleFactor()

    LaunchedEffect(true) {
        insetsController.isAppearanceLightStatusBars = false
    }

    LaunchedEffect(Unit) {
        if (initialAdvice.isNotEmpty()) {
            viewModel.setInitAdvice(initialAdvice)
        }
    }

        Log.d("RecompositionTracker", "AdviceScreen recomposed")

    AdviceScreen(
        activity,
        state,
        textScale,
        onEvent = { event -> viewModel.handleEvent(event) }
    )
}

@Composable
private fun AdviceScreen(
    activity: Activity?,
    state: AdviceUIState,
    textScale: Float,
    onEvent: (AdviceEvent?) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black)
    ) {
        Image(
            painter = painterResource(R.drawable.space_meatballs),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.title_advices),
                fontSize = 22.adaptiveSp(textScale),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 60.dp)
            )

            AnimatedText(Modifier, activity, state, textScale, onEvent)

            GetCompliment(
                Modifier.align(Alignment.CenterHorizontally),
                state,
                textScale
            ) { event ->
                onEvent(event)
            }
        }

        if (state.showDialog) {
            OfferingDialog (
                onDonate = {
                    onEvent(AdviceEvent.ShowAdDialog(isShow = false, isAdEnable = true))
                },
                onDismiss = {
                    onEvent(AdviceEvent.ShowAdDialog(isShow = false, isAdEnable = false))
                }
            )
        }
    }
}

@Composable
fun AnimatedText(
    modifier: Modifier,
    activity: Activity?,
    state: AdviceUIState,
    textScale: Float,
    onEvent: (AdviceEvent) -> Unit
) {
    AnimatedVisibility(
        visible = state.isTextVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable {
                    val adviceText = state.advice
                    activity?.showToast(adviceText)
                    onEvent(AdviceEvent.CopyAdvice(adviceText))
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.advice,
                style = LocalTextStyle.current.copy(
                    fontSize = 38.adaptiveSp(textScale),
                    lineHeight = 38.adaptiveSp(textScale),
                    color = MaterialTheme.colorScheme.onSecondary,
                    textAlign = TextAlign.Center
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GetCompliment(
    modifier: Modifier,
    state: AdviceUIState,
    textScale: Float,
    onClick: (AdviceEvent?) -> Unit
) {
    Log.i("ADVICE_TAG", "UI state.adviceCount ${state.adviceCount}")
    val buttonText = when(state.buttonState){
        is ButtonUiState.Loading -> stringResource(R.string.loading_ads)
        is ButtonUiState.Advice -> stringResource(R.string.get_advice)
        is ButtonUiState.Offering -> stringResource(R.string.get_ads)
        is ButtonUiState.IsOver -> stringResource(R.string.is_over)
    }

    val event = when(state.buttonState){
        is ButtonUiState.Loading -> null
        is ButtonUiState.Advice -> AdviceEvent.GetAdvice
        is ButtonUiState.Offering -> null
        is ButtonUiState.IsOver -> AdviceEvent.ShowAdDialog(isShow = true, isAdEnable = false)
    }

    val buttonModifier = if (textScale < 1.2f){
        Modifier.aspectRatio(1.4f)
            .padding(bottom = 120.dp, start = 24.dp, end = 24.dp)
    } else{
        Modifier
            .padding(bottom = 120.dp, start = 24.dp, end = 24.dp)
            .height(160.dp)
            .width(340.dp)
    }

    VolumetricButton(
        modifier = modifier.then(buttonModifier),
        shape = RoundedCornerShape(100.dp),
        firstColor = ColorMeatball,
        secondColor = ColorMeatballDark,
        onTouch = { onClick(event) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = buttonText,
            fontSize = 28.adaptiveSp(textScale),
            lineHeight = 34.adaptiveSp(textScale),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}
