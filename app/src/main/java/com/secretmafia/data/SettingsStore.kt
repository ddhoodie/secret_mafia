package com.secretmafia.data

import android.content.Context
import android.provider.Settings
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.secretmafia.game.AppLang
import com.secretmafia.game.AppSettings
import com.secretmafia.game.CoinKind
import com.secretmafia.game.DayVoteMode
import com.secretmafia.game.DummyActionType
import com.secretmafia.game.GameRules
import com.secretmafia.game.Profile
import com.secretmafia.game.Progress
import com.secretmafia.game.Role
import com.secretmafia.game.RoleChances
import com.secretmafia.game.RoleCounts
import com.secretmafia.game.Wallet
import com.secretmafia.game.WalletGuard
import com.secretmafia.game.WhoreAlign
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "secret_mafia_settings")

class SettingsStore(private val context: Context) {
    private val showVote = booleanPreferencesKey("show_mafia_vote_count")
    private val discussMins = intPreferencesKey("discuss_timer_minutes")
    private val dummy = stringPreferencesKey("dummy_action")
    private val revealRole = booleanPreferencesKey("reveal_role_on_death")
    private val firstKill = booleanPreferencesKey("first_night_kill")
    private val healerRepeat = booleanPreferencesKey("healer_may_repeat")
    private val framerFoolsSeer = booleanPreferencesKey("framer_fools_seer")
    private val seerSeesTraitor = booleanPreferencesKey("seer_sees_traitor")
    private val inspectSameNight = booleanPreferencesKey("inspect_same_night")
    private val dayVote = stringPreferencesKey("day_vote_mode")
    private val sound = booleanPreferencesKey("sound_enabled")
    private val vibration = booleanPreferencesKey("vibration_enabled")
    private val narrator = booleanPreferencesKey("narrator_enabled")
    private val light = booleanPreferencesKey("light_theme")
    private val lang = stringPreferencesKey("language")
    private val hideColors = booleanPreferencesKey("hide_game_colors")
    private val revealEnd = booleanPreferencesKey("reveal_roles_at_end")
    private val whoreAlign = stringPreferencesKey("whore_align")
    private val mafiaConfer = booleanPreferencesKey("mafia_confer")
    private val mafiaConferSecs = intPreferencesKey("mafia_confer_seconds")
    private val blood = intPreferencesKey("blood")
    private val town = intPreferencesKey("town")
    private val gold = intPreferencesKey("gold")
    private val unlockedRoles = stringPreferencesKey("unlocked_roles")
    private val walletBlob = stringPreferencesKey("q7")
    private val adWatchesKey = intPreferencesKey("ad_watches")
    private val profileName = stringPreferencesKey("profile_name")
    private val avatarId = intPreferencesKey("avatar_id")
    private val playSignedIn = booleanPreferencesKey("play_signed_in")
    private val dummyRandomSet = booleanPreferencesKey("dummy_random_v1")
    private val starterApplied = booleanPreferencesKey("starter_pack_v1")
    private val playerNamesKey = stringPreferencesKey("player_names")
    private val setupCountsKey = stringPreferencesKey("setup_counts")
    private val setupAdvancedKey = booleanPreferencesKey("setup_advanced")
    private val setupChancesKey = stringPreferencesKey("setup_chances")

    val settings: Flow<AppSettings> = context.dataStore.data.map { it.toSettings() }
    val wallet: Flow<Wallet> = context.dataStore.data.map { it.toWallet() }
    val profile: Flow<Profile> = context.dataStore.data.map { it.toProfile() }
    val playerNames: Flow<List<String>> = context.dataStore.data.map { it.toPlayerNames() }
    val setupCounts: Flow<RoleCounts?> = context.dataStore.data.map {
        RoleCounts.decode(it[setupCountsKey].orEmpty())
    }
    val setupAdvanced: Flow<Boolean> = context.dataStore.data.map { it[setupAdvancedKey] ?: false }
    val setupChances: Flow<RoleChances> = context.dataStore.data.map {
        RoleChances.decode(it[setupChancesKey].orEmpty())
    }
    val adWatches: Flow<Int> = context.dataStore.data.map {
        (it[adWatchesKey] ?: 0).coerceIn(0, Progress.ADS_FOR_COIN)
    }

    suspend fun sealWalletIfNeeded() {
        context.dataStore.edit { p ->
            var wallet = p.toWallet()
            if (p[starterApplied] != true) {
                val debugStack = wallet.blood == Progress.DEBUG_START_STACK &&
                    wallet.town == Progress.DEBUG_START_STACK &&
                    wallet.gold == Progress.DEBUG_START_STACK
                wallet = if (debugStack) {
                    wallet.copy(
                        blood = Progress.START_BLOOD,
                        town = Progress.START_TOWN,
                        gold = Progress.START_GOLD,
                    )
                } else {
                    wallet.copy(
                        blood = maxOf(wallet.blood, Progress.START_BLOOD),
                        town = maxOf(wallet.town, Progress.START_TOWN),
                        gold = maxOf(wallet.gold, Progress.START_GOLD),
                    )
                }
                p[starterApplied] = true
            }
            if (p[dummyRandomSet] != true) {
                p[dummy] = DummyActionType.RANDOM.name
                p[dummyRandomSet] = true
            }
            p.writeWallet(wallet)
        }
    }

    suspend fun update(transform: (AppSettings) -> AppSettings) {
        context.dataStore.edit { p ->
            val next = transform(p.toSettings())
            p[showVote] = next.showMafiaVoteCount
            p[discussMins] = next.discussTimerMinutes
            p[dummy] = next.dummyActionType.name
            p[revealRole] = next.revealRoleOnDeath
            p[firstKill] = next.firstNightKill
            p[healerRepeat] = next.healerMayRepeatTarget
            p[framerFoolsSeer] = next.framerFoolsSeer
            p[seerSeesTraitor] = next.seerSeesTraitor
            p[inspectSameNight] = next.inspectSameNight
            p[dayVote] = next.dayVoteMode.name
            p[sound] = next.soundEnabled
            p[vibration] = next.vibrationEnabled
            p[narrator] = next.narratorEnabled
            p[light] = next.lightTheme
            p[lang] = next.language.name
            p[hideColors] = next.hideGameColors
            p[revealEnd] = next.revealRolesAtEnd
            p[whoreAlign] = next.whoreAlign.name
            p[mafiaConfer] = next.mafiaConfer
            p[mafiaConferSecs] = next.mafiaConferSeconds
        }
    }

    suspend fun setProfile(next: Profile) {
        context.dataStore.edit { p ->
            p[profileName] = next.name
            p[avatarId] = next.avatarId.coerceIn(0, Progress.AVATAR_COUNT - 1)
            p[playSignedIn] = next.playSignedIn
        }
    }

    suspend fun setPlayerNames(names: List<String>) {
        val cleaned = names
            .map { it.trim().take(16) }
            .let { list ->
                when {
                    list.size < GameRules.MIN_PLAYERS ->
                        list + List(GameRules.MIN_PLAYERS - list.size) { "" }
                    list.size > GameRules.MAX_PLAYERS -> list.take(GameRules.MAX_PLAYERS)
                    else -> list
                }
            }
        context.dataStore.edit { p ->
            p[playerNamesKey] = cleaned.joinToString("\u0001")
        }
    }

    suspend fun setSetup(counts: RoleCounts, advanced: Boolean, chances: RoleChances = RoleChances()) {
        context.dataStore.edit { p ->
            p[setupCountsKey] = counts.encode()
            p[setupAdvancedKey] = advanced
            p[setupChancesKey] = chances.encode()
        }
    }

    suspend fun clearSetup() {
        context.dataStore.edit { p ->
            p[playerNamesKey] = List(6) { "" }.joinToString("\u0001")
            p[setupCountsKey] = GameRules.recommendedCounts(6).encode()
            p[setupAdvancedKey] = false
            p[setupChancesKey] = ""
        }
    }

    suspend fun addCoin(kind: CoinKind, n: Int = 1) {
        context.dataStore.edit { p ->
            p.writeWallet(Progress.grant(p.toWallet(), kind, n))
        }
    }

    suspend fun recordAdWatch() {
        context.dataStore.edit { p ->
            p[adWatchesKey] = Progress.bumpAdWatches(p[adWatchesKey] ?: 0)
        }
    }

    suspend fun claimAdCoin(kind: CoinKind) {
        context.dataStore.edit { p ->
            if (!Progress.adPickReady(p[adWatchesKey] ?: 0)) return@edit
            p.writeWallet(Progress.grant(p.toWallet(), kind, Progress.AD_REWARD))
            p[adWatchesKey] = 0
        }
    }

    suspend fun unlockRole(role: Role): Boolean {
        var ok = false
        context.dataStore.edit { p ->
            val next = Progress.unlock(p.toWallet(), role) ?: return@edit
            p.writeWallet(next)
            ok = true
        }
        return ok
    }

    suspend fun applyCloud(wallet: Wallet, profile: Profile) {
        context.dataStore.edit { p ->
            p.writeWallet(wallet)
            p[profileName] = profile.name.take(16)
            p[avatarId] = profile.avatarId.coerceIn(0, Progress.AVATAR_COUNT - 1)
            p[playSignedIn] = true
        }
    }

    private fun androidx.datastore.preferences.core.Preferences.toSettings(): AppSettings {
        val p = this
        return AppSettings(
            showMafiaVoteCount = p[showVote] ?: true,
            discussTimerMinutes = p[discussMins] ?: 0,
            dummyActionType = runCatching {
                DummyActionType.valueOf(p[dummy] ?: DummyActionType.RANDOM.name)
            }.getOrDefault(DummyActionType.RANDOM),
            revealRoleOnDeath = p[revealRole] ?: false,
            firstNightKill = p[firstKill] ?: true,
            healerMayRepeatTarget = p[healerRepeat] ?: false,
            framerFoolsSeer = p[framerFoolsSeer] ?: false,
            seerSeesTraitor = p[seerSeesTraitor] ?: false,
            inspectSameNight = p[inspectSameNight] ?: false,
            dayVoteMode = runCatching {
                DayVoteMode.valueOf(p[dayVote] ?: DayVoteMode.LIVE.name)
            }.getOrDefault(DayVoteMode.LIVE),
            soundEnabled = p[sound] ?: true,
            vibrationEnabled = p[vibration] ?: true,
            narratorEnabled = p[narrator] ?: true,
            lightTheme = p[light] ?: false,
            language = runCatching {
                AppLang.valueOf(p[lang] ?: AppLang.EN.name)
            }.getOrDefault(AppLang.EN),
            hideGameColors = p[hideColors] ?: false,
            revealRolesAtEnd = p[revealEnd] ?: true,
            whoreAlign = runCatching {
                WhoreAlign.valueOf(p[whoreAlign] ?: WhoreAlign.PICK.name)
            }.getOrDefault(WhoreAlign.PICK),
            mafiaConfer = p[mafiaConfer] ?: false,
            mafiaConferSeconds = (p[mafiaConferSecs] ?: 12).coerceIn(8, 30),
        )
    }

    private fun androidx.datastore.preferences.core.Preferences.toWallet(): Wallet {
        val sealed = this[walletBlob]
        if (sealed != null) return WalletGuard.open(sealed, deviceId()) ?: Wallet()
        val names = this[unlockedRoles].orEmpty()
        val fallbackBlood = Progress.START_BLOOD
        val fallbackTown = Progress.START_TOWN
        val fallbackGold = Progress.START_GOLD
        return Wallet(
            blood = this[blood] ?: fallbackBlood,
            town = this[town] ?: fallbackTown,
            gold = this[gold] ?: fallbackGold,
            unlocked = names.split(",")
                .mapNotNull { runCatching { Role.valueOf(it) }.getOrNull() }
                .toSet(),
        )
    }

    private fun androidx.datastore.preferences.core.MutablePreferences.writeWallet(w: Wallet) {
        this[walletBlob] = WalletGuard.seal(w, deviceId())
        this.remove(blood)
        this.remove(town)
        this.remove(gold)
        this.remove(unlockedRoles)
    }

    private fun deviceId(): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            .orEmpty()
            .ifBlank { "none" }

    private fun androidx.datastore.preferences.core.Preferences.toProfile(): Profile = Profile(
        name = this[profileName].orEmpty(),
        avatarId = this[avatarId] ?: 0,
        playSignedIn = this[playSignedIn] ?: false,
    )

    private fun androidx.datastore.preferences.core.Preferences.toPlayerNames(): List<String> {
        val raw = this[playerNamesKey].orEmpty()
        if (raw.isBlank()) return List(GameRules.MIN_PLAYERS.coerceAtLeast(6)) { "" }
        val parts = raw.split('\u0001').map { it.take(16) }
        return when {
            parts.size < GameRules.MIN_PLAYERS ->
                parts + List(GameRules.MIN_PLAYERS - parts.size) { "" }
            parts.size > GameRules.MAX_PLAYERS -> parts.take(GameRules.MAX_PLAYERS)
            else -> parts
        }
    }
}
