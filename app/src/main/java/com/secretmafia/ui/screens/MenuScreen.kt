package com.secretmafia.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.secretmafia.game.Profile
import com.secretmafia.game.Wallet
import com.secretmafia.ui.components.AvatarPortrait
import com.secretmafia.ui.components.BrandLogo
import com.secretmafia.ui.components.CoinLine
import com.secretmafia.ui.components.PixelButton
import com.secretmafia.ui.components.PixelScreen
import com.secretmafia.ui.components.PixelText
import com.secretmafia.ui.components.VSpace
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str

@Composable
fun MenuScreen(
    wallet: Wallet,
    profile: Profile,
    onProfile: () -> Unit,
    onPlay: () -> Unit,
    onRoles: () -> Unit,
    onSettings: () -> Unit,
    onRules: () -> Unit,
    onStats: () -> Unit,
    onAbout: () -> Unit,
) {
    val s = str()
    val c = pal()
    PixelScreen(scroll = true) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, c.fg)
                .clickable(onClick = onProfile)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AvatarPortrait(profile.avatarId, size = 52.dp)
            Column(Modifier.weight(1f)) {
                PixelText(
                    profile.displayName.ifBlank { s.guest },
                    size = 16,
                    bold = true,
                    align = TextAlign.Start,
                )
                VSpace(4.dp)
                CoinLine(wallet, size = 12)
            }
        }
        VSpace(20.dp)
        BrandLogo(size = 140.dp, blink = true)
        VSpace(12.dp)
        PixelText(s.brandTitleLine2, size = 34, bold = true)
        PixelText(s.tagline, size = 14, color = c.accent)
        VSpace(24.dp)
        PixelButton(s.play, onClick = onPlay)
        VSpace(12.dp)
        PixelButton(s.rules, onClick = onRules)
        VSpace(12.dp)
        PixelButton(s.roles, onClick = onRoles)
        VSpace(12.dp)
        PixelButton(s.settings, onClick = onSettings)
        VSpace(12.dp)
        PixelButton(s.statistics, onClick = onStats)
        VSpace(12.dp)
        PixelButton(s.about, onClick = onAbout)
    }
}
