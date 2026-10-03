package com.secretmafia.ui.components

import android.view.SoundEffectConstants
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.secretmafia.ui.theme.LocalFont
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
        .windowInsetsPadding(WindowInsets.statusBars.union(WindowInsets.displayCutout))
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
    val font = LocalFont.current
    val wide = font == FontFamily.SansSerif
    Text(
        text = text,
        modifier = modifier,
        color = if (color == Color.Unspecified) c.fg else color,
        fontFamily = font,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        fontSize = size.sp,
        textAlign = align,
        lineHeight = (size + if (wide) 10 else 6).sp,
    )
}

@Composable
fun PixelButton(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accent: Boolean = false,
    tint: Color? = null,
    onClick: () -> Unit,
) {
    val c = pal()
    val view = LocalView.current
    val haptics = LocalHapticFeedback.current
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val paint = tint ?: if (accent) c.accent else c.fg
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .alpha(if (enabled) 1f else 0.35f)
            .border(2.dp, paint, RectangleShape)
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
        PixelText(label.uppercase(), color = paint, size = 18, bold = true)
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
    val hop = rememberInfiniteTransition(label = "drain-hop")
    val lift by hop.animateFloat(
        initialValue = 0f,
        targetValue = -9f,
        animationSpec = infiniteRepeatable(tween(240), RepeatMode.Reverse),
        label = "lift",
    )
    Box(
        Modifier
            .offset(y = lift.dp)
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
    hint: String? = null,
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
    if (!hint.isNullOrBlank()) {
        Spacer(Modifier.height(6.dp))
        PixelText(hint, size = 13, color = c.muted, align = TextAlign.Start, modifier = Modifier.fillMaxWidth())
    }
    Spacer(Modifier.height(10.dp))
}

@Composable
fun Stepper(
    label: String,
    value: Int,
    min: Int = 0,
    max: Int = 16,
    step: Int = 1,
    valueText: String? = null,
    hint: String? = null,
    onLabelClick: (() -> Unit)? = null,
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
        val labelMod = Modifier.weight(1f).let { base ->
            if (onLabelClick == null) base else base.clickable(onClick = onLabelClick)
        }
        PixelText(label, size = 16, bold = true, align = TextAlign.Start, modifier = labelMod)
        MiniBtn("-") { if (value > min) onChange((value - step).coerceAtLeast(min)) }
        Box(Modifier.widthIn(min = 48.dp), contentAlignment = Alignment.Center) {
            PixelText(valueText ?: value.toString(), size = 16, bold = true)
        }
        MiniBtn("+") { if (value < max) onChange((value + step).coerceAtMost(max)) }
    }
    if (!hint.isNullOrBlank()) {
        Spacer(Modifier.height(6.dp))
        PixelText(hint, size = 13, color = c.muted, align = TextAlign.Start, modifier = Modifier.fillMaxWidth())
    }
    Spacer(Modifier.height(10.dp))
}

@Composable
internal fun MiniBtn(text: String, onClick: () -> Unit) {
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

@Composable
fun GameBar(
    narratorOn: Boolean,
    onNarrator: () -> Unit,
    onHome: () -> Unit,
) {
    val c = pal()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.bg)
            .padding(start = 22.dp, end = 22.dp, top = 8.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ChromeButton(Modifier.weight(1f), onClick = onNarrator) {
            SpeakerMark(on = narratorOn, color = c.fg, bg = c.bg)
        }
        ChromeButton(Modifier.weight(1f), onClick = onHome) {
            HouseMark(color = c.fg, bg = c.bg)
        }
    }
}

@Composable
private fun ChromeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    val c = pal()
    val view = LocalView.current
    val haptics = LocalHapticFeedback.current
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    Box(
        modifier = modifier
            .height(52.dp)
            .border(2.dp, c.fg, RectangleShape)
            .background(if (pressed) c.press else c.bg)
            .clickable(
                interactionSource = interaction,
                indication = null,
            ) {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        icon()
    }
}

@Composable
private fun SpeakerMark(on: Boolean, color: Color, bg: Color, size: Dp = 28.dp) {
    Canvas(Modifier.size(size)) {
        val w = this.size.minDimension
        val box = Path().apply {
            moveTo(w * 0.10f, w * 0.36f)
            lineTo(w * 0.32f, w * 0.36f)
            lineTo(w * 0.32f, w * 0.64f)
            lineTo(w * 0.10f, w * 0.64f)
            close()
        }
        val cone = Path().apply {
            moveTo(w * 0.32f, w * 0.36f)
            lineTo(w * 0.58f, w * 0.16f)
            lineTo(w * 0.58f, w * 0.84f)
            lineTo(w * 0.32f, w * 0.64f)
            close()
        }
        drawPath(box, color)
        drawPath(cone, color)
        val stroke = Stroke(width = w * 0.10f, cap = StrokeCap.Square)
        if (on) {
            drawArc(
                color = color,
                startAngle = -40f,
                sweepAngle = 80f,
                useCenter = false,
                topLeft = Offset(w * 0.50f, w * 0.28f),
                size = Size(w * 0.38f, w * 0.44f),
                style = stroke,
            )
            drawArc(
                color = color,
                startAngle = -40f,
                sweepAngle = 80f,
                useCenter = false,
                topLeft = Offset(w * 0.62f, w * 0.18f),
                size = Size(w * 0.34f, w * 0.64f),
                style = stroke,
            )
        } else {
            drawLine(
                color = bg,
                start = Offset(w * 0.10f, w * 0.10f),
                end = Offset(w * 0.90f, w * 0.90f),
                strokeWidth = w * 0.22f,
                cap = StrokeCap.Square,
            )
            drawLine(
                color = color,
                start = Offset(w * 0.12f, w * 0.12f),
                end = Offset(w * 0.88f, w * 0.88f),
                strokeWidth = w * 0.12f,
                cap = StrokeCap.Square,
            )
        }
    }
}

@Composable
private fun HouseMark(color: Color, bg: Color, size: Dp = 28.dp) {
    Canvas(Modifier.size(size)) {
        val w = this.size.minDimension
        val roof = Path().apply {
            moveTo(w * 0.50f, w * 0.08f)
            lineTo(w * 0.92f, w * 0.46f)
            lineTo(w * 0.08f, w * 0.46f)
            close()
        }
        val body = Path().apply {
            moveTo(w * 0.20f, w * 0.46f)
            lineTo(w * 0.80f, w * 0.46f)
            lineTo(w * 0.80f, w * 0.92f)
            lineTo(w * 0.20f, w * 0.92f)
            close()
        }
        drawPath(roof, color)
        drawPath(body, color)
        drawRect(
            color = bg,
            topLeft = Offset(w * 0.42f, w * 0.62f),
            size = Size(w * 0.16f, w * 0.30f),
        )
    }
}
