package com.secretmafia.ui.components

import android.view.SoundEffectConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.secretmafia.ui.theme.PixelFamily
import com.secretmafia.ui.theme.pal
import kotlinx.coroutines.delay

@Composable
fun PixelScreen(
    modifier: Modifier = Modifier,
    scroll: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val c = pal()
    val inner = Modifier
        .fillMaxSize()
        .background(c.bg)
        .padding(horizontal = 22.dp, vertical = 28.dp)
    Column(
        modifier = if (scroll) inner.verticalScroll(rememberScrollState()) else inner,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content,
    )
}

@Composable
fun PixelText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    size: Int = 18,
    bold: Boolean = false,
    align: TextAlign = TextAlign.Center,
) {
    val c = pal()
    Text(
        text = text,
        modifier = modifier,
        color = if (color == Color.Unspecified) c.fg else color,
        fontFamily = PixelFamily,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        fontSize = size.sp,
        textAlign = align,
        lineHeight = (size + 6).sp,
    )
}

@Composable
fun PixelButton(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accent: Boolean = false,
    onClick: () -> Unit,
) {
    val c = pal()
    val view = LocalView.current
    val haptics = LocalHapticFeedback.current
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val border = if (accent) c.accent else c.fg
    val text = if (accent) c.accent else c.fg
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .alpha(if (enabled) 1f else 0.35f)
            .border(2.dp, border, RectangleShape)
            .background(if (pressed && enabled) c.press else c.bg)
            .clickable(
                enabled = enabled,
                interactionSource = interaction,
                indication = null,
            ) {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        PixelText(label.uppercase(), color = text, size = 18, bold = true)
    }
}

@Composable
fun HoldUnlock(
    label: String,
    holdMillis: Long = 900L,
    onUnlocked: () -> Unit,
) {
    val c = pal()
    val haptics = LocalHapticFeedback.current
    var holding by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    val animated by animateFloatAsState(progress, label = "hold")

    LaunchedEffect(holding) {
        if (!holding) {
            progress = 0f
            return@LaunchedEffect
        }
        val start = System.currentTimeMillis()
        while (holding) {
            val p = (System.currentTimeMillis() - start) / holdMillis.toFloat()
            progress = p.coerceIn(0f, 1f)
            if (p >= 1f) {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                onUnlocked()
                break
            }
            delay(16)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(2.dp, c.fg, RectangleShape)
            .pointerInput(holdMillis) {
                detectTapGestures(
                    onPress = {
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        holding = true
                        try {
                            tryAwaitRelease()
                        } finally {
                            holding = false
                        }
                    },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Box(
                Modifier
                    .fillMaxWidth(animated)
                    .fillMaxSize()
                    .background(c.holdFill),
            )
        }
        PixelText(label, size = 16, bold = true)
    }
}

@Composable
fun DrainBar(
    millis: Long = 3000L,
    color: Color = Color.Unspecified,
    onFinished: () -> Unit,
) {
    val c = pal()
    val bar = if (color == Color.Unspecified) c.accent else color
    var progress by remember { mutableFloatStateOf(1f) }
    LaunchedEffect(millis) {
        val start = System.currentTimeMillis()
        while (true) {
            val left = 1f - ((System.currentTimeMillis() - start) / millis.toFloat())
            progress = left.coerceIn(0f, 1f)
            if (progress <= 0f) break
            delay(16)
        }
        onFinished()
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(10.dp)
            .border(1.dp, bar, RectangleShape),
    ) {
        Box(
            Modifier
                .fillMaxWidth(progress)
                .height(10.dp)
                .background(bar),
        )
    }
}

@Composable
fun SettingRow(
    title: String,
    value: String,
    onClick: () -> Unit,
) {
    val c = pal()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, c.fg, RectangleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PixelText(title, size = 15, bold = true, align = TextAlign.Start, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(12.dp))
        PixelText(value, size = 15)
    }
    Spacer(Modifier.height(10.dp))
}

@Composable
fun Stepper(
    label: String,
    value: Int,
    min: Int = 0,
    max: Int = 16,
    onChange: (Int) -> Unit,
) {
    val c = pal()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, c.fg)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        PixelText(label, size = 16, bold = true, align = TextAlign.Start, modifier = Modifier.weight(1f))
        MiniBtn("-") { if (value > min) onChange(value - 1) }
        Box(Modifier.width(36.dp), contentAlignment = Alignment.Center) {
            PixelText(value.toString(), size = 20, bold = true)
        }
        MiniBtn("+") { if (value < max) onChange(value + 1) }
    }
}

@Composable
private fun MiniBtn(text: String, onClick: () -> Unit) {
    val c = pal()
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(40.dp)
            .border(2.dp, c.fg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        PixelText(text, size = 22, bold = true)
    }
}

@Composable
fun VSpace(height: Dp = 16.dp) = Spacer(Modifier.height(height))
