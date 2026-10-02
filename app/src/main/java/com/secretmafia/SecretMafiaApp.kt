package com.secretmafia

import android.graphics.drawable.ColorDrawable
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.secretmafia.data.SettingsStore
import com.secretmafia.game.AppSettings
import com.secretmafia.game.CoinKind
import com.secretmafia.game.GameRules
import com.secretmafia.game.GameViewModel
import com.secretmafia.game.Progress
import com.secretmafia.game.GamePhase
import com.secretmafia.game.Profile
import com.secretmafia.game.Role
import com.secretmafia.game.RoleChances
import com.secretmafia.game.RoleCounts
import com.secretmafia.game.Wallet
import com.secretmafia.ui.components.BootSplash
import com.secretmafia.ui.screens.AboutScreen
import com.secretmafia.ui.screens.AnimDebugScreen
import com.secretmafia.ui.screens.ComingSoonScreen
import com.secretmafia.ui.screens.PrivacyScreen
import com.secretmafia.ui.screens.ProfileScreen
import com.secretmafia.ui.screens.RoleUnlockDetail
import com.secretmafia.ui.screens.RolesCatalogScreen
import com.secretmafia.ui.screens.AppearanceScreen
import com.secretmafia.ui.screens.GameScreen
import com.secretmafia.ui.screens.GameplayDayScreen
import com.secretmafia.ui.screens.GameplayHub
import com.secretmafia.ui.screens.GameplayNightScreen
import com.secretmafia.ui.screens.MenuScreen
import com.secretmafia.ui.screens.RulesHub
import com.secretmafia.ui.screens.RulesRoleDetail
import com.secretmafia.ui.screens.RulesRolesList
import com.secretmafia.ui.screens.RulesTextScreen
import com.secretmafia.ui.screens.SettingsHub
import com.secretmafia.ui.screens.SetupScreen
import com.secretmafia.ui.theme.ProvideAppStyle
import com.secretmafia.ui.theme.SecretMafiaTheme
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SecretMafiaApp(
    store: SettingsStore,
    gameVm: GameViewModel = viewModel(),
) {
    val settings by store.settings.collectAsStateWithLifecycle(initialValue = AppSettings())
    val wallet by store.wallet.collectAsStateWithLifecycle(initialValue = Wallet())
    val profile by store.profile.collectAsStateWithLifecycle(initialValue = Profile())
    var playerNames by remember { mutableStateOf<List<String>?>(null) }
    var setupCounts by remember { mutableStateOf<RoleCounts?>(null) }
    var setupAdvanced by remember { mutableStateOf(false) }
    var setupChances by remember { mutableStateOf(RoleChances()) }
    var setupRevision by remember { mutableStateOf(0) }
    val game by gameVm.state.collectAsStateWithLifecycle()
    val nav = rememberNavController()
    var booting by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as ComponentActivity
    val playCloud = (activity.application as MafiaApp).playCloud
    var playStatus by remember { mutableStateOf("") }
    val save: (AppSettings) -> Unit = { next -> scope.launch { store.update { next } } }
    val saveProfile: (Profile) -> Unit = { next -> scope.launch { store.setProfile(next) } }
    val persistSetup: (RoleCounts, Boolean, RoleChances) -> Unit = { counts, advanced, chances ->
        scope.launch { store.setSetup(counts, advanced, chances) }
    }

    LaunchedEffect(store) {
        store.sealWalletIfNeeded()
        val names = store.playerNames.first()
        val counts = store.setupCounts.first()
        val advanced = store.setupAdvanced.first()
        val chances = store.setupChances.first()
        playerNames = names
        setupCounts = counts ?: GameRules.recommendedCounts(names.size.coerceAtLeast(6))
        setupAdvanced = advanced
        setupChances = chances
    }

    LaunchedEffect(wallet, setupCounts) {
        val cur = setupCounts ?: return@LaunchedEffect
        val next = cur.onlyOwned(wallet::owns)
        if (next != cur) {
            setupCounts = next
            persistSetup(next, setupAdvanced, setupChances)
        }
    }
    LaunchedEffect(game?.phase, game?.matchReward, game?.matchPaid, game?.survivorLived) {
        val s = game ?: return@LaunchedEffect
        if (s.phase !is GamePhase.GameOver || s.matchPaid) return@LaunchedEffect
        s.matchReward?.let { Progress.coinFrom(it) }?.let { kind ->
            store.addCoin(kind, Progress.MATCH_REWARD)
        }
        if (s.survivorLived) store.addCoin(CoinKind.GOLD, Progress.MATCH_REWARD)
        gameVm.markMatchPaid()
        if (profile.playSignedIn) {
            var next = wallet
            s.matchReward?.let { Progress.coinFrom(it) }?.let { next = Progress.grant(next, it, Progress.MATCH_REWARD) }
            if (s.survivorLived) next = Progress.grant(next, CoinKind.GOLD, Progress.MATCH_REWARD)
            playCloud.save(activity, next, profile)
        }
    }

    SecretMafiaTheme {
        ProvideAppStyle(settings) {
            val screen = pal().bg
            SideEffect {
                activity.window.setBackgroundDrawable(ColorDrawable(screen.toArgb()))
            }
            Box(Modifier.fillMaxSize().background(screen)) {
            NavHost(navController = nav, startDestination = "menu", modifier = Modifier.fillMaxSize()) {
                composable("menu") {
                    MenuScreen(
                        wallet = wallet,
                        profile = profile,
                        onProfile = { nav.navigate("profile") },
                        onPlay = { nav.navigate("setup") },
                        onRoles = { nav.navigate("roles") },
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
                composable("settings/debug") {
                    AnimDebugScreen { nav.popBackStack() }
                }
                composable("settings/appearance") {
                    AppearanceScreen(settings, save) { nav.popBackStack() }
                }
                composable("settings/gameplay") {
                    GameplayHub(
                        onNight = { nav.navigate("settings/gameplay/night") },
                        onDay = { nav.navigate("settings/gameplay/day") },
                        onBack = { nav.popBackStack() },
                    )
                }
                composable("settings/gameplay/night") {
                    GameplayNightScreen(settings, save) { nav.popBackStack() }
                }
                composable("settings/gameplay/day") {
                    GameplayDayScreen(settings, save) { nav.popBackStack() }
                }
                composable("profile") {
                    val s = str()
                    ProfileScreen(
                        profile = profile,
                        status = playStatus,
                        playReady = playCloud.configured(),
                        onName = { saveProfile(profile.copy(name = it)) },
                        onAvatar = { saveProfile(profile.copy(avatarId = it)) },
                        onSignIn = {
                            scope.launch {
                                playStatus = ""
                                val ok = playCloud.signIn(activity)
                                saveProfile(profile.copy(playSignedIn = ok))
                                playStatus = if (ok) {
                                    val loaded = playCloud.load(activity)
                                    if (loaded != null) {
                                        store.applyCloud(loaded.first, loaded.second)
                                        s.playLoaded
                                    } else {
                                        playCloud.save(activity, wallet, profile.copy(playSignedIn = true))
                                        s.signedIn
                                    }
                                } else {
                                    s.playSignInFail
                                }
                            }
                        },
                        onSave = {
                            scope.launch {
                                playStatus = if (playCloud.save(activity, wallet, profile)) s.playSaved else s.playNeedSignIn
                            }
                        },
                        onLoad = {
                            scope.launch {
                                val loaded = playCloud.load(activity)
                                playStatus = if (loaded != null) {
                                    store.applyCloud(loaded.first, loaded.second)
                                    s.playLoaded
                                } else {
                                    s.playEmpty
                                }
                            }
                        },
                        onBack = { nav.popBackStack() },
                    )
                }
                composable("roles") {
                    RolesCatalogScreen(
                        wallet = wallet,
                        onRole = { nav.navigate("roles/${it.name}") },
                        onBack = { nav.popBackStack() },
                    )
                }
                composable(
                    "roles/{role}",
                    arguments = listOf(navArgument("role") { type = NavType.StringType }),
                ) { entry ->
                    val role = runCatching {
                        Role.valueOf(entry.arguments?.getString("role") ?: "")
                    }.getOrNull()
                    if (role == null) {
                        LaunchedEffect(Unit) { nav.popBackStack() }
                    } else {
                        RoleUnlockDetail(
                            role = role,
                            wallet = wallet,
                            whoreAlign = settings.whoreAlign,
                            onCycleWhoreAlign = {
                                save(settings.copy(whoreAlign = settings.whoreAlign.next()))
                            },
                            mayRepeatTarget = settings.healerMayRepeatTarget,
                            onToggleRepeatTarget = {
                                save(settings.copy(healerMayRepeatTarget = !settings.healerMayRepeatTarget))
                            },
                            onUnlock = { scope.launch { store.unlockRole(role) } },
                            onBack = { nav.popBackStack() },
                        )
                    }
                }
                composable("about") {
                    AboutScreen(
                        onPrivacy = { nav.navigate("about/privacy") },
                        onBack = { nav.popBackStack() },
                    )
                }
                composable("about/privacy") {
                    PrivacyScreen { nav.popBackStack() }
                }
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
                    val saved = playerNames
                    val counts = setupCounts
                    if (saved == null || counts == null) return@composable
                    SetupScreen(
                        counts = counts,
                        onCounts = {
                            setupCounts = it
                            persistSetup(it, setupAdvanced, setupChances)
                        },
                        chances = setupChances,
                        onChances = {
                            setupChances = it
                            val cur = setupCounts ?: return@SetupScreen
                            persistSetup(cur, setupAdvanced, it)
                        },
                        advancedOpen = setupAdvanced,
                        onAdvancedOpen = { open ->
                            setupAdvanced = open
                            val cur = setupCounts ?: return@SetupScreen
                            persistSetup(cur, open, setupChances)
                        },
                        savedNames = saved,
                        onNamesChange = { next ->
                            playerNames = next
                            scope.launch { store.setPlayerNames(next) }
                        },
                        wallet = wallet,
                        onUnlockRole = { role -> scope.launch { store.unlockRole(role) } },
                        narratorEnabled = settings.narratorEnabled,
                        onNarratorToggle = { save(settings.copy(narratorEnabled = !settings.narratorEnabled)) },
                        onClearSetup = {
                            scope.launch {
                                store.clearSetup()
                                playerNames = List(6) { "" }
                                setupCounts = GameRules.recommendedCounts(6)
                                setupAdvanced = false
                                setupChances = RoleChances()
                                setupRevision += 1
                            }
                        },
                        setupRevision = setupRevision,
                        onStart = { names, roleCounts, chances ->
                            gameVm.start(names, roleCounts.onlyOwned(wallet::owns), settings, chances)
                            nav.navigate("game")
                        },
                        onBack = { nav.popBackStack() },
                    )
                }
                composable("game") {
                    val state = game
                    if (state == null) {
                        LaunchedEffect(Unit) {
                            nav.navigate("menu") {
                                popUpTo("menu") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    } else {
                        GameScreen(
                            vm = gameVm,
                            state = state,
                            wallet = wallet,
                            onQuit = {
                                nav.navigate("menu") {
                                    popUpTo("menu") { inclusive = true }
                                    launchSingleTop = true
                                }
                                gameVm.clear()
                            },
                            onNarratorChange = { on ->
                                gameVm.patchSettings { it.copy(narratorEnabled = on) }
                                save(settings.copy(narratorEnabled = on))
                            },
                        )
                    }
                }
            }
            if (booting) BootSplash { booting = false }
            }
        }
    }
}
