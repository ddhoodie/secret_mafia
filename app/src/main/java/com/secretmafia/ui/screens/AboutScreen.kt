package com.secretmafia.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.secretmafia.ui.components.BrandLogo
import com.secretmafia.ui.components.PixelButton
import com.secretmafia.ui.components.PixelScreen
import com.secretmafia.ui.components.PixelText
import com.secretmafia.ui.components.VSpace
import com.secretmafia.ui.theme.str

@Composable
fun ComingSoonScreen(title: String, body: String, onBack: () -> Unit) {
    val s = str()
    PixelScreen {
        PixelText(title, size = 28, bold = true)
        VSpace(12.dp)
        PixelText(s.comingSoon, size = 16)
        VSpace(16.dp)
        PixelText(body, size = 16)
        VSpace(28.dp)
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
fun AboutScreen(onBack: () -> Unit) {
    val s = str()
    PixelScreen(scroll = true) {
        PixelText(s.about, size = 28, bold = true)
        VSpace(20.dp)
        BrandLogo(size = 120.dp)
        VSpace(12.dp)
        PixelText(s.brandTitleLine1, size = 18, bold = true)
        PixelText(s.brandTitleLine2, size = 26, bold = true)
        VSpace(20.dp)
        PixelText(s.about1, size = 16)
        VSpace(16.dp)
        PixelText(s.about2, size = 16)
        VSpace(16.dp)
        PixelText(s.about3, size = 16)
        VSpace(28.dp)
        PixelButton(s.back, onClick = onBack)
    }
}
