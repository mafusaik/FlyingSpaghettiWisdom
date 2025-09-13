package com.glazer.flying.spaghetti.monster.gospel.bible

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.DEFAULT_LANGUAGE
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.KEY_LANGUAGE
import com.glazer.flying.spaghetti.monster.gospel.bible.domain.constants.AppConstants.PREFERENCES_NAME_TOKEN
import com.glazer.flying.spaghetti.monster.gospel.bible.extensions.setAppLocale
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.advice.AdviceScreen
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.advice.AdviceViewModel
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.book.BookViewModel
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.book.BookScreen
import com.glazer.flying.spaghetti.monster.gospel.bible.navigation.BottomNavigation
import com.glazer.flying.spaghetti.monster.gospel.bible.navigation.BottomScreens
import com.glazer.flying.spaghetti.monster.gospel.bible.navigation.NavHostContainer
import com.glazer.flying.spaghetti.monster.gospel.bible.navigation.NavigationViewModel
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.settings.SettingsScreen
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.settings.SettingsViewModel
import com.glazer.flying.spaghetti.monster.gospel.bible.ui.theme.PastafarianTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val navigationViewModel: NavigationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null && Build.VERSION.SDK_INT >= 35) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        if (Build.VERSION.SDK_INT < 35) {
            WindowCompat.getInsetsController(window, window.decorView).apply {
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                hide(WindowInsetsCompat.Type.navigationBars())
            }
        }

        enableEdgeToEdge()

        val window = this.window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                mainViewModel.checkRecreate()
            }

            PastafarianTheme {
                val navController = rememberNavController()
                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route
                val bottomScreens = remember {
                    listOf(BottomScreens.Book, BottomScreens.Advice, BottomScreens.Settings)
                }
                val listRoutes = bottomScreens.map { it.route }

                LaunchedEffect(Unit) {
                    navigationViewModel.navigationEvents.collect { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize(),
                        bottomBar = {
                            BottomNavigation(currentRoute, bottomScreens, onNavigate = { route ->
                                navigationViewModel.navigateTo(route)
                            })
                        },
                        content = { padding ->
                            NavHostContainer(
                                navController = navController,
                                listRoutes = listRoutes,
                                composableContent = appNavGraph(insetsController),
                                padding = padding
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun attachBaseContext(newBase: Context) {
        Log.i("MAIN_ACTIVITY", "attachBaseContext")
        val languageCode = getLanguageCode(newBase)
        val overrideConfiguration = Configuration(newBase.resources.configuration)
        overrideConfiguration.fontScale = 1.0f
        val newContext = newBase.createConfigurationContext(overrideConfiguration)
        val lang = languageCode.ifEmpty { Locale.getDefault().language }
        super.attachBaseContext(newContext.setAppLocale(lang))
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (Build.VERSION.SDK_INT < 35 && hasFocus) {
            hideSystemUI()
        }
    }

    private fun handleIntent(intent: Intent) {
        Log.i("INTENT_TAG", "handleIntent ${intent.action} ${intent.data}")
        when {
            intent.action == Intent.ACTION_VIEW -> {
                val uri = intent.data
                if (uri?.scheme == "myapp" && uri.host == "deepLink") {
                    uri.pathSegments.firstOrNull()?.let { destination ->
                        navigationViewModel.navigateTo(destination)
                    }
                }
            }
        }
    }

    private fun appNavGraph(
        insetsController: WindowInsetsControllerCompat
    ): NavGraphBuilder.() -> Unit = {
        composable(BottomScreens.Book.route) {
            val bookViewModel: BookViewModel = hiltViewModel()
            BookScreen(bookViewModel, insetsController)
        }
        composable(BottomScreens.Advice.route) {
            val adviceViewModel: AdviceViewModel = hiltViewModel()
            AdviceScreen(adviceViewModel, "", insetsController)
        }
        composable(BottomScreens.Settings.route) {
            val settingViewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(settingViewModel, insetsController)
        }
    }

    private fun getLanguageCode(newBase: Context): String {
        val sharedPreferences = newBase.applicationContext.getSharedPreferences(
            PREFERENCES_NAME_TOKEN,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE)
            ?: DEFAULT_LANGUAGE
    }

    private fun hideSystemUI() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.navigationBars())
        }
    }
}

