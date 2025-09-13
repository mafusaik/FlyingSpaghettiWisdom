package com.glazer.flying.spaghetti.monster.gospel.bible.navigation

import com.glazer.flying.spaghetti.monster.gospel.bible.R


sealed class BottomScreens(val name: String, val icon: Int, val route: String) {
    data object Book : BottomScreens(name = "Book", icon = R.drawable.icon_book, route = "BookScreen")
    data object Advice : BottomScreens(name = "Advice", icon = R.drawable.icon_fsm, route = "AdviceScreen")
    data object Settings : BottomScreens(name = "Settings", icon = R.drawable.icon_settings, route = "SettingScreen")
}