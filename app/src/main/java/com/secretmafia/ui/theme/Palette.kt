package com.secretmafia.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.secretmafia.game.AppLang
import com.secretmafia.game.AppSettings
import com.secretmafia.ui.i18n.Str

data class Palette(
    val bg: Color,
    val fg: Color,
    val muted: Color,
    val accent: Color,
    val heal: Color,
    val press: Color,
    val holdFill: Color,
)

fun buildPalette(light: Boolean, hideGameColors: Boolean, inGame: Boolean): Palette {
    val bg = if (light) Paper else Ink
    val fg = if (light) Ink else Paper
    val hide = hideGameColors && inGame
    return Palette(
        bg = bg,
        fg = fg,
        muted = if (light) Color(0xFF555555) else Color(0xFFBBBBBB),
        accent = if (hide) fg else Blood,
        heal = if (hide) fg else Heal,
        press = if (light) Color(0x11000000) else Color(0x22FFFFFF),
        holdFill = if (light) Color(0x22000000) else Color(0x22FFFFFF),
    )
}

val LocalPalette = staticCompositionLocalOf { buildPalette(false, false, false) }
val LocalStr = staticCompositionLocalOf { Str(AppLang.EN) }
val LocalInGame = staticCompositionLocalOf { false }

@Composable
fun ProvideAppStyle(
    settings: AppSettings,
    inGame: Boolean = false,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalPalette provides buildPalette(settings.lightTheme, settings.hideGameColors, inGame),
        LocalStr provides Str(settings.language),
        LocalInGame provides inGame,
        content = content,
    )
}

@Composable
fun pal() = LocalPalette.current

@Composable
fun str() = LocalStr.current
