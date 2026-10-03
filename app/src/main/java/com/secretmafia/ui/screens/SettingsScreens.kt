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
import com.secretmafia.ui.components.Stepper
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
        SettingRow(s.lightTheme, onOff(s, settings.lightTheme), s.lightThemeHint) {
            onChange(settings.copy(lightTheme = !settings.lightTheme))
        }
        SettingRow(s.language, settings.language.nativeName, s.languageHint) {
            onChange(settings.copy(language = settings.language.next()))
        }
        SettingRow(s.hideColors, onOff(s, settings.hideGameColors), s.hideColorsHint) {
            onChange(settings.copy(hideGameColors = !settings.hideGameColors))
        }
        VSpace()
        SettingRow(s.sound, onOff(s, settings.soundEnabled), s.soundHint) {
            onChange(settings.copy(soundEnabled = !settings.soundEnabled))
        }
        SettingRow(s.vibration, onOff(s, settings.vibrationEnabled), s.vibrationHint) {
            onChange(settings.copy(vibrationEnabled = !settings.vibrationEnabled))
        }
        SettingRow(s.narrator, onOff(s, settings.narratorEnabled), s.narratorHint) {
            onChange(settings.copy(narratorEnabled = !settings.narratorEnabled))
        }
        VSpace()
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
fun GameplayHub(
    onNight: () -> Unit,
    onDay: () -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    PixelScreen {
        PixelText(s.gameplay, size = 28, bold = true)
        VSpace(24.dp)
        PixelButton(s.round, onClick = onNight)
        VSpace(12.dp)
        PixelButton(s.day, onClick = onDay)
        VSpace(28.dp)
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
        SettingRow(s.firstKill, onOff(s, settings.firstNightKill), s.firstKillHint) {
            onChange(settings.copy(firstNightKill = !settings.firstNightKill))
        }
        SettingRow(s.sameNightLook, onOff(s, settings.inspectSameNight), s.sameNightLookHint) {
            onChange(settings.copy(inspectSameNight = !settings.inspectSameNight))
        }
        SettingRow(s.mafiaConfer, onOff(s, settings.mafiaConfer), s.mafiaConferHint) {
            onChange(settings.copy(mafiaConfer = !settings.mafiaConfer))
        }
        if (settings.mafiaConfer) {
            VSpace(8.dp)
            SettingRow(s.mafiaTalkTime, s.secLabel(settings.mafiaConferSeconds), s.mafiaTalkTimeHint) {
                onChange(settings.copy(mafiaConferSeconds = nextConferSecs(settings.mafiaConferSeconds)))
            }
        }
        SettingRow(s.voteCount, onOff(s, settings.showMafiaVoteCount), s.voteCountHint) {
            onChange(settings.copy(showMafiaVoteCount = !settings.showMafiaVoteCount))
        }
        SettingRow(s.dummyAction, dummyLabel(s, settings.dummyActionType), s.dummyActionHint) {
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
        Stepper(
            label = s.discussTimer,
            value = settings.discussTimerMinutes,
            min = 0,
            max = 30,
            valueText = s.minLabel(settings.discussTimerMinutes),
            hint = s.discussTimerHint,
        ) {
            onChange(settings.copy(discussTimerMinutes = it))
        }
        SettingRow(
            s.dayVote,
            if (settings.dayVoteMode == DayVoteMode.LIVE) s.live else s.phone,
            s.dayVoteHint,
        ) {
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
        SettingRow(s.revealRole, onOff(s, settings.revealRoleOnDeath), s.revealRoleHint) {
            onChange(settings.copy(revealRoleOnDeath = !settings.revealRoleOnDeath))
        }
        SettingRow(s.revealEnd, onOff(s, settings.revealRolesAtEnd), s.revealEndHint) {
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
