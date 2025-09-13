package com.glazer.flying.spaghetti.monster.gospel.bible.navigation

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.ColorSpaghetti

@Composable
fun BottomNavigation(
    currentRoute: String?,
    bottomScreens: List<BottomScreens>,
    onNavigate: (String) -> Unit
) {
    val bottomPadding = if (Build.VERSION.SDK_INT < 35) 24.dp
    else 0.dp
    Box(
        modifier = Modifier
            .padding(bottom = bottomPadding, start = 24.dp, end = 24.dp)
            .navigationBarsPadding()
            .clip(RoundedCornerShape(32.dp))
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            containerColor = ColorSpaghetti.copy(alpha = 0.5f),
            tonalElevation = 8.dp
        ) {
            bottomScreens.forEach { screen ->
                MyNavItem(
                    screen = screen,
                    isSelected = currentRoute == screen.route,
                    onClick = {
                        onNavigate(screen.route)
                    }
                )
            }
        }
    }
}

@Composable
fun RowScope.MyNavItem(
    screen: BottomScreens,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else Color.Transparent,
        animationSpec = tween(durationMillis = 100), label = ""
    )

    NavigationBarItem(
        icon = { Icon(painterResource(screen.icon), contentDescription = null) },
        selected = isSelected,
        colors = NavigationBarItemColors(
            selectedIndicatorColor = Color.Transparent,
            selectedIconColor = MaterialTheme.colorScheme.background,
            unselectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
            unselectedTextColor = MaterialTheme.colorScheme.primary,
            disabledIconColor = Color.LightGray,
            disabledTextColor = Color.LightGray
        ),
        onClick = onClick,
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
    )
}