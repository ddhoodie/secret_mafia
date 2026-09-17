package com.secretmafia.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.secretmafia.game.AppSettings
import com.secretmafia.game.DayVoteMode
import com.secretmafia.game.DummyActionType
import com.secretmafia.ui.components.PixelButton
import com.secretmafia.ui.components.PixelScreen
import com.secretmafia.ui.components.PixelText
import com.secretmafia.ui.components.SettingRow
import com.secretmafia.ui.components.VSpace
import com.secretmafia.ui.i18n.Str
import com.secretmafia.ui.theme.str

@Composable
fun SettingsHub(
    onAppearance: () -> Unit,
    onGameplay: () -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    PixelScreen {
        PixelText(s.settings, size = 28, bold = true)
        VSpace(24.dp)
        PixelButton(s.appearance, onClick = onAppearance)
        VSpace(12.dp)
        PixelButton(s.gameplay, onClick = onGameplay)
        VSpace(24.dp)
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
fun AppearanceScreen(
    settings: AppSettings,
    onChange: (AppSettings) -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    PixelScreen(scroll = true) {
        PixelText(s.appearance, size = 28, bold = true)
        VSpace()
        SettingRow(s.lightTheme, onOff(s, settings.lightTheme)) {
            onChange(settings.copy(lightTheme = !settings.lightTheme))
        }
        SettingRow(s.language, settings.language.nativeName) {
            onChange(settings.copy(language = settings.language.next()))
        }
        SettingRow(s.hideColors, onOff(s, settings.hideGameColors)) {
            onChange(settings.copy(hideGameColors = !settings.hideGameColors))
        }
        PixelText(s.hideColorsHint, size = 13)
        VSpace()
        SettingRow(s.sound, onOff(s, settings.soundEnabled)) {
            onChange(settings.copy(soundEnabled = !settings.soundEnabled))
        }
        SettingRow(s.vibration, onOff(s, settings.vibrationEnabled)) {
            onChange(settings.copy(vibrationEnabled = !settings.vibrationEnabled))
        }
        SettingRow(s.narrator, onOff(s, settings.narratorEnabled)) {
            onChange(settings.copy(narratorEnabled = !settings.narratorEnabled))
        }
        VSpace()
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
fun GameplayHub(
    onRoles: () -> Unit,
    onNight: () -> Unit,
    onDay: () -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    PixelScreen {
        PixelText(s.gameplay, size = 28, bold = true)
        VSpace(24.dp)
        PixelButton(s.roles, onClick = onRoles)
        VSpace(12.dp)
        PixelButton(s.round, onClick = onNight)
        VSpace(12.dp)
        PixelButton(s.day, onClick = onDay)
        VSpace(28.dp)
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
fun GameplayRolesScreen(
    settings: AppSettings,
    onChange: (AppSettings) -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    PixelScreen(scroll = true) {
        PixelText(s.roles, size = 28, bold = true)
        VSpace()
        SettingRow(s.healerRepeat, onOff(s, settings.healerMayRepeatTarget)) {
            onChange(settings.copy(healerMayRepeatTarget = !settings.healerMayRepeatTarget))
        }
        VSpace()
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
fun GameplayNightScreen(
    settings: AppSettings,
    onChange: (AppSettings) -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    PixelScreen(scroll = true) {
        PixelText(s.night, size = 28, bold = true)
        VSpace()
        SettingRow(s.firstKill, onOff(s, settings.firstNightKill)) {
            onChange(settings.copy(firstNightKill = !settings.firstNightKill))
        }
        SettingRow(s.mafiaConfer, onOff(s, settings.mafiaConfer)) {
            onChange(settings.copy(mafiaConfer = !settings.mafiaConfer))
        }
        PixelText(s.mafiaConferHint, size = 13)
        if (settings.mafiaConfer) {
            VSpace(8.dp)
            SettingRow(s.mafiaTalkTime, s.secLabel(settings.mafiaConferSeconds)) {
                onChange(settings.copy(mafiaConferSeconds = nextConferSecs(settings.mafiaConferSeconds)))
            }
        }
        SettingRow(s.voteCount, onOff(s, settings.showMafiaVoteCount)) {
            onChange(settings.copy(showMafiaVoteCount = !settings.showMafiaVoteCount))
        }
        SettingRow(s.dummyAction, dummyLabel(s, settings.dummyActionType)) {
            onChange(settings.copy(dummyActionType = nextDummy(settings.dummyActionType)))
        }
        VSpace()
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
fun GameplayDayScreen(
    settings: AppSettings,
    onChange: (AppSettings) -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    PixelScreen(scroll = true) {
        PixelText(s.day, size = 28, bold = true)
        VSpace()
        SettingRow(s.discussTimer, s.minLabel(settings.discussTimerMinutes)) {
            onChange(settings.copy(discussTimerMinutes = nextDiscuss(settings.discussTimerMinutes)))
        }
        SettingRow(s.dayVote, if (settings.dayVoteMode == DayVoteMode.LIVE) s.live else s.phone) {
            onChange(
                settings.copy(
                    dayVoteMode = if (settings.dayVoteMode == DayVoteMode.LIVE) {
                        DayVoteMode.PHONE
                    } else {
                        DayVoteMode.LIVE
                    },
                ),
            )
        }
        SettingRow(s.revealRole, onOff(s, settings.revealRoleOnDeath)) {
            onChange(settings.copy(revealRoleOnDeath = !settings.revealRoleOnDeath))
        }
        SettingRow(s.revealEnd, onOff(s, settings.revealRolesAtEnd)) {
            onChange(settings.copy(revealRolesAtEnd = !settings.revealRolesAtEnd))
        }
        VSpace()
        PixelButton(s.back, onClick = onBack)
    }
}

private fun onOff(s: Str, on: Boolean) = if (on) s.on else s.off

private fun dummyLabel(s: Str, type: DummyActionType) = when (type) {
    DummyActionType.LIKE -> s.like
    DummyActionType.MATH -> s.math
    DummyActionType.RANDOM -> s.random
}

private fun nextDiscuss(current: Int): Int = when (current) {
    0 -> 2
    2 -> 3
    3 -> 5
    5 -> 8
    else -> 0
}

private fun nextDummy(current: DummyActionType): DummyActionType = when (current) {
    DummyActionType.LIKE -> DummyActionType.MATH
    DummyActionType.MATH -> DummyActionType.RANDOM
    DummyActionType.RANDOM -> DummyActionType.LIKE
}

private fun nextConferSecs(current: Int): Int = when (current) {
    8 -> 12
    12 -> 16
    16 -> 20
    20 -> 24
    24 -> 30
    else -> 8
}
