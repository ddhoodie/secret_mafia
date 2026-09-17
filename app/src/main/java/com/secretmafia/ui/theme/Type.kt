package com.secretmafia.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.secretmafia.R
import com.secretmafia.game.AppLang

val PixelFamily = FontFamily(
    Font(R.font.pixel_operator, FontWeight.Normal),
    Font(R.font.pixel_operator_bold, FontWeight.Bold),
)

fun uiFont(lang: AppLang): FontFamily = when (lang) {
    AppLang.ZH -> FontFamily.SansSerif
    else -> PixelFamily
}

val LocalFont = staticCompositionLocalOf { PixelFamily }
