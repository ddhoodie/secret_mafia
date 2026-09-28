package com.secretmafia.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.secretmafia.game.Profile
import com.secretmafia.game.Progress
import com.secretmafia.ui.components.AvatarPortrait
import com.secretmafia.ui.components.PixelButton
import com.secretmafia.ui.components.PixelScreen
import com.secretmafia.ui.components.PixelText
import com.secretmafia.ui.components.VSpace
import com.secretmafia.ui.theme.LocalFont
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str

@Composable
fun ProfileScreen(
    profile: Profile,
    status: String,
    playReady: Boolean,
    onName: (String) -> Unit,
    onAvatar: (Int) -> Unit,
    onSignIn: () -> Unit,
    onSave: () -> Unit,
    onLoad: () -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    val c = pal()
    PixelScreen(scroll = true) {
        PixelText(s.profile, size = 28, bold = true)
        VSpace(8.dp)
        PixelText(s.playHint, size = 13, color = c.muted)
        VSpace(16.dp)
        AvatarPortrait(profile.avatarId, size = 88.dp)
        VSpace(12.dp)
        PixelText(s.pickAvatar, size = 13)
        VSpace(8.dp)
        (0 until Progress.AVATAR_COUNT).chunked(4).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEach { i ->
                    val selected = profile.avatarId == i
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(if (selected) 3.dp else 2.dp, if (selected) c.accent else c.fg)
                            .clickable { onAvatar(i) }
                            .padding(6.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        AvatarPortrait(i, size = 48.dp)
                    }
                }
            }
            VSpace(8.dp)
        }
        VSpace(16.dp)
        PixelText(s.name, size = 12, align = TextAlign.Start, modifier = Modifier.fillMaxWidth())
        VSpace(4.dp)
        BasicTextField(
            value = profile.name,
            onValueChange = { if (it.length <= 16) onName(it) },
            singleLine = true,
            cursorBrush = SolidColor(c.fg),
            textStyle = TextStyle(
                color = c.fg,
                fontFamily = LocalFont.current,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, c.fg)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            decorationBox = { inner ->
                if (profile.name.isEmpty()) {
                    PixelText(s.namePlaceholder, size = 16, color = c.muted, align = TextAlign.Start)
                }
                inner()
            },
        )
        VSpace(16.dp)
        if (profile.playSignedIn) {
            PixelText(s.signedIn, size = 14, color = c.muted)
            VSpace(8.dp)
            PixelButton(s.savePlay, onClick = onSave)
            VSpace(8.dp)
            PixelButton(s.loadPlay, onClick = onLoad)
        } else {
            PixelButton(s.signInPlay, enabled = playReady, onClick = onSignIn)
            if (!playReady) {
                VSpace(8.dp)
                PixelText(s.playNotLinked, size = 13, color = c.muted)
            }
        }
        if (status.isNotEmpty()) {
            VSpace(10.dp)
            PixelText(status, size = 13, color = c.muted)
        }
        VSpace(22.dp)
        PixelButton(s.back, onClick = onBack)
    }
}
