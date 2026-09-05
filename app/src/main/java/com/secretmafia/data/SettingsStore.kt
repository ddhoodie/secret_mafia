package com.secretmafia.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.secretmafia.game.AppLang
import com.secretmafia.game.AppSettings
import com.secretmafia.game.DayVoteMode
import com.secretmafia.game.DummyActionType
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
    private val dayVote = stringPreferencesKey("day_vote_mode")
    private val sound = booleanPreferencesKey("sound_enabled")
    private val light = booleanPreferencesKey("light_theme")
    private val lang = stringPreferencesKey("language")
    private val hideColors = booleanPreferencesKey("hide_game_colors")
    private val revealEnd = booleanPreferencesKey("reveal_roles_at_end")

    val settings: Flow<AppSettings> = context.dataStore.data.map { it.toSettings() }

    suspend fun update(transform: (AppSettings) -> AppSettings) {
        context.dataStore.edit { p ->
            val next = transform(p.toSettings())
            p[showVote] = next.showMafiaVoteCount
            p[discussMins] = next.discussTimerMinutes
            p[dummy] = next.dummyActionType.name
            p[revealRole] = next.revealRoleOnDeath
            p[firstKill] = next.firstNightKill
            p[healerRepeat] = next.healerMayRepeatTarget
            p[dayVote] = next.dayVoteMode.name
            p[sound] = next.soundEnabled
            p[light] = next.lightTheme
            p[lang] = next.language.name
            p[hideColors] = next.hideGameColors
            p[revealEnd] = next.revealRolesAtEnd
        }
    }

    private fun androidx.datastore.preferences.core.Preferences.toSettings(): AppSettings {
        val p = this
        return AppSettings(
            showMafiaVoteCount = p[showVote] ?: true,
            discussTimerMinutes = p[discussMins] ?: 0,
            dummyActionType = runCatching {
                DummyActionType.valueOf(p[dummy] ?: DummyActionType.LIKE.name)
            }.getOrDefault(DummyActionType.LIKE),
            revealRoleOnDeath = p[revealRole] ?: false,
            firstNightKill = p[firstKill] ?: true,
            healerMayRepeatTarget = p[healerRepeat] ?: false,
            dayVoteMode = runCatching {
                DayVoteMode.valueOf(p[dayVote] ?: DayVoteMode.LIVE.name)
            }.getOrDefault(DayVoteMode.LIVE),
            soundEnabled = p[sound] ?: true,
            lightTheme = p[light] ?: false,
            language = runCatching {
                AppLang.valueOf(p[lang] ?: AppLang.EN.name)
            }.getOrDefault(AppLang.EN),
            hideGameColors = p[hideColors] ?: false,
            revealRolesAtEnd = p[revealEnd] ?: true,
        )
    }
}
