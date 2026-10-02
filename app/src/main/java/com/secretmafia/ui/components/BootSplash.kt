package com.secretmafia.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.secretmafia.ui.theme.pal
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun BootSplash(onFinished: () -> Unit) {
    val c = pal()
    val clock = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        clock.animateTo(1f, tween(1200, easing = LinearEasing))
        alpha.animateTo(0f, tween(200, easing = LinearEasing))
        onFinished()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { this.alpha = alpha.value }
            .background(c.bg),
        contentAlignment = Alignment.Center,
    ) {
        BrandLogo(size = 168.dp)
        BloodPool(
            t = clock.value,
            blood = c.accent,
            modifier = Modifier
                .offset(y = 208.dp)
                .size(width = 176.dp, height = 208.dp),
        )
    }
}

private val Drop = intArrayOf(
    0, -3,
    -1, -2, 0, -2, 1, -2,
    -1, -1, 0, -1, 1, -1,
    0, 0,
)

private data class Spray(val vx: Float, val vy: Float, val fat: Boolean)

private val Sprays = listOf(
    Spray(-6.4f, -28f, true),
    Spray(6.6f, -32f, true),
    Spray(-3.1f, -42f, false),
    Spray(3.3f, -38f, false),
    Spray(-1.1f, -22f, false),
    Spray(1.4f, -24f, false),
)

@Composable
private fun BloodPool(t: Float, blood: Color, modifier: Modifier = Modifier) {
    val deep = Color(0xFF4A0000)
    val hot = Color(0xFFFF3A2A)
    Canvas(modifier) {
        drawBlood(t, blood, deep, hot)
    }
}

private fun DrawScope.drawBlood(t: Float, blood: Color, deep: Color, hot: Color) {
    val cols = 11
    val rows = 13
    val cell = size.width / cols
    val ox = (size.width - cell * cols) / 2f
    val oy = (size.height - cell * rows) / 2f
    val cx = 5
    val surface = 11
    fun px(col: Int, row: Int, color: Color) {
        if (col !in 0 until cols || row !in 0 until rows) return
        drawRect(color, Offset(ox + col * cell, oy + row * cell), Size(cell, cell))
    }

    val fallEnd = 0.42f
    val hit = if (t <= fallEnd) 0f else ((t - fallEnd) / (1f - fallEnd)).coerceIn(0f, 1f)
    val grow = 1f - (1f - hit) * (1f - hit)
    val half = 1.2f + grow * 3.4f
    for (dx in -5..5) {
        if (abs(dx) > half) continue
        val edge = abs(dx) / 5f
        val color = when {
            hit in 0.02f..0.2f && abs(dx) <= 1 -> hot
            edge > 0.62f -> deep
            else -> blood
        }
        px(cx + dx, surface, color)
        if (abs(dx) <= half - 1f) px(cx + dx, surface + 1, deep)
    }

    if (t < fallEnd) {
        val p = t / fallEnd
        val tip = (2f + p * p * 8f).roundToInt()
        var i = 0
        while (i < Drop.size) {
            val dx = Drop[i]
            val dy = Drop[i + 1]
            val color = if (dx == -1 && dy == -2) hot else blood
            px(cx + dx, tip + dy, color)
            i += 2
        }
    } else {
        val g = 48f
        Sprays.forEach { spray ->
            val x = cx + spray.vx * hit
            val y = surface + spray.vy * hit + g * hit * hit
            if (y >= surface - 0.15f) return@forEach
            val col = x.roundToInt()
            val row = y.roundToInt()
            px(col, row, if (spray.fat) blood else hot)
            if (spray.fat) px(col, row - 1, blood)
        }
    }
}
