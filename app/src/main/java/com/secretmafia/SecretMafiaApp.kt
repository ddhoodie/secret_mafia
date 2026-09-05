package com.secretmafia

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.secretmafia.data.SettingsStore
import com.secretmafia.game.AppSettings
import com.secretmafia.game.GameRules
import com.secretmafia.game.GameViewModel
import com.secretmafia.game.Role
import com.secretmafia.game.RoleCounts
import com.secretmafia.ui.screens.AboutScreen
import com.secretmafia.ui.screens.ComingSoonScreen
import com.secretmafia.ui.screens.AdvancedRolesScreen
import com.secretmafia.ui.screens.AppearanceScreen
import com.secretmafia.ui.screens.GameScreen
import com.secretmafia.ui.screens.GameplayDayScreen
import com.secretmafia.ui.screens.GameplayHub
import com.secretmafia.ui.screens.GameplayNightScreen
import com.secretmafia.ui.screens.GameplayRolesScreen
import com.secretmafia.ui.screens.MenuScreen
import com.secretmafia.ui.screens.RulesHub
import com.secretmafia.ui.screens.RulesRoleDetail
import com.secretmafia.ui.screens.RulesRolesList
import com.secretmafia.ui.screens.RulesTextScreen
import com.secretmafia.ui.screens.SettingsHub
import com.secretmafia.ui.screens.SetupScreen
import com.secretmafia.ui.theme.ProvideAppStyle
import com.secretmafia.ui.theme.SecretMafiaTheme
import com.secretmafia.ui.theme.str
import kotlinx.coroutines.launch

@Composable
fun SecretMafiaApp(
    store: SettingsStore,
    gameVm: GameViewModel = viewModel(),
) {
    val settings by store.settings.collectAsStateWithLifecycle(initialValue = AppSettings())
    val game by gameVm.state.collectAsStateWithLifecycle()
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()
    var setupCounts by remember { mutableStateOf(GameRules.recommendedCounts(6)) }
    var setupPlayers by remember { mutableStateOf(6) }
    val save: (AppSettings) -> Unit = { next -> scope.launch { store.update { next } } }

    SecretMafiaTheme {
        ProvideAppStyle(settings) {
            NavHost(navController = nav, startDestination = "menu") {
                composable("menu") {
                    MenuScreen(
                        onPlay = { nav.navigate("setup") },
                        onSettings = { nav.navigate("settings") },
                        onRules = { nav.navigate("rules") },
                        onStats = { nav.navigate("stats") },
                        onAbout = { nav.navigate("about") },
                    )
                }
                composable("settings") {
                    SettingsHub(
                        onAppearance = { nav.navigate("settings/appearance") },
                        onGameplay = { nav.navigate("settings/gameplay") },
                        onBack = { nav.popBackStack() },
                    )
                }
                composable("settings/appearance") {
                    AppearanceScreen(settings, save) { nav.popBackStack() }
                }
                composable("settings/gameplay") {
                    GameplayHub(
                        onRoles = { nav.navigate("settings/gameplay/roles") },
                        onNight = { nav.navigate("settings/gameplay/night") },
                        onDay = { nav.navigate("settings/gameplay/day") },
                        onBack = { nav.popBackStack() },
                    )
                }
                composable("settings/gameplay/roles") {
                    GameplayRolesScreen(settings, save) { nav.popBackStack() }
                }
                composable("settings/gameplay/night") {
                    GameplayNightScreen(settings, save) { nav.popBackStack() }
                }
                composable("settings/gameplay/day") {
                    GameplayDayScreen(settings, save) { nav.popBackStack() }
                }
                composable("about") { AboutScreen { nav.popBackStack() } }
                composable("stats") {
                    val s = str()
                    ComingSoonScreen(s.statistics, s.statsSoon) { nav.popBackStack() }
                }
                composable("rules") {
                    val s = str()
                    RulesHub(
                        onGeneral = { nav.navigate("rules/general") },
                        onRoles = { nav.navigate("rules/roles") },
                        onNight = { nav.navigate("rules/night") },
                        onDay = { nav.navigate("rules/day") },
                        onWinning = { nav.navigate("rules/winning") },
                        onBack = { nav.popBackStack() },
                    )
                }
                composable("rules/general") {
                    val s = str()
                    RulesTextScreen(s.general, s.generalBody) { nav.popBackStack() }
                }
                composable("rules/night") {
                    val s = str()
                    RulesTextScreen(s.night, s.nightBody) { nav.popBackStack() }
                }
                composable("rules/day") {
                    val s = str()
                    RulesTextScreen(s.day, s.dayBody) { nav.popBackStack() }
                }
                composable("rules/winning") {
                    val s = str()
                    RulesTextScreen(s.winning, s.winBody) { nav.popBackStack() }
                }
                composable("rules/roles") {
                    RulesRolesList(
                        onRole = { nav.navigate("rules/roles/${it.name}") },
                        onBack = { nav.popBackStack() },
                    )
                }
                composable(
                    "rules/roles/{role}",
                    arguments = listOf(navArgument("role") { type = NavType.StringType }),
                ) { entry ->
                    val role = runCatching {
                        Role.valueOf(entry.arguments?.getString("role") ?: "")
                    }.getOrNull()
                    if (role == null) {
                        LaunchedEffect(Unit) { nav.popBackStack() }
                    } else {
                        RulesRoleDetail(role) { nav.popBackStack() }
                    }
                }
                composable("setup") {
                    SetupScreen(
                        counts = setupCounts,
                        onCounts = { setupCounts = it },
                        onPlayerCount = { setupPlayers = it },
                        onStart = { names, counts ->
                            gameVm.start(names, counts, settings)
                            nav.navigate("game")
                        },
                        onAdvanced = { nav.navigate("setup/advanced") },
                        onBack = { nav.popBackStack() },
                    )
                }
                composable("setup/advanced") {
                    AdvancedRolesScreen(setupCounts, setupPlayers) {
                        setupCounts = it
                        nav.popBackStack()
                    }
                }
                composable("game") {
                    val state = game
                    if (state == null) {
                        LaunchedEffect(Unit) { nav.popBackStack() }
                    } else {
                        BackHandler {
                            gameVm.clear()
                            nav.popBackStack()
                        }
                        GameScreen(gameVm, state) {
                            gameVm.clear()
                            nav.popBackStack("menu", inclusive = false)
                        }
                    }
                }
            }
        }
    }
}
