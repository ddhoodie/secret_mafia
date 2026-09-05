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
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit,
        )
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
