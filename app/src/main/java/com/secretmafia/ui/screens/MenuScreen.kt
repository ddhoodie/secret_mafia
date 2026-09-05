package com.secretmafia.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.secretmafia.R
import com.secretmafia.ui.components.PixelButton
import com.secretmafia.ui.components.PixelScreen
import com.secretmafia.ui.components.PixelText
import com.secretmafia.ui.components.VSpace
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onSettings: () -> Unit,
    onRules: () -> Unit,
    onStats: () -> Unit,
    onAbout: () -> Unit,
) {
    val s = str()
    val c = pal()
    PixelScreen {
        VSpace(28.dp)
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Secret Mafia logo",
            modifier = Modifier.size(168.dp),
            contentScale = ContentScale.Fit,
        )
        VSpace(16.dp)
        PixelText("SECRET MAFIA", size = 34, bold = true)
        PixelText(s.tagline, size = 14, color = c.accent)
        VSpace(32.dp)
        PixelButton(s.play, onClick = onPlay)
        VSpace(12.dp)
        PixelButton(s.rules, onClick = onRules)
        VSpace(12.dp)
        PixelButton(s.settings, onClick = onSettings)
        VSpace(12.dp)
        PixelButton(s.statistics, onClick = onStats)
        VSpace(12.dp)
        PixelButton(s.about, onClick = onAbout)
    }
}
