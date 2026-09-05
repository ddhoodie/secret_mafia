package com.secretmafia.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Scheme = darkColorScheme(
    primary = Paper,
    onPrimary = Ink,
    background = Ink,
    onBackground = Paper,
    surface = Ink,
    onSurface = Paper,
    error = Blood,
    onError = Paper,
)

private val Type = Typography(
    displayLarge = TextStyle(fontFamily = PixelFamily, fontWeight = FontWeight.Bold, fontSize = 42.sp, color = Paper),
    displayMedium = TextStyle(fontFamily = PixelFamily, fontWeight = FontWeight.Bold, fontSize = 32.sp, color = Paper),
    headlineMedium = TextStyle(fontFamily = PixelFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Paper),
    titleLarge = TextStyle(fontFamily = PixelFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Paper),
    bodyLarge = TextStyle(fontFamily = PixelFamily, fontWeight = FontWeight.Normal, fontSize = 18.sp, color = Paper),
    bodyMedium = TextStyle(fontFamily = PixelFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Paper),
    labelLarge = TextStyle(fontFamily = PixelFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Paper),
)

@Composable
fun SecretMafiaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Scheme, typography = Type, content = content)
}
