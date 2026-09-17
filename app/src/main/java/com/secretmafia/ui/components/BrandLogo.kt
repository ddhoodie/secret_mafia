package com.secretmafia.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.secretmafia.R
import com.secretmafia.ui.theme.Paper
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str
import kotlin.random.Random
import kotlinx.coroutines.delay

@Composable
fun BrandLogo(
    modifier: Modifier = Modifier,
    size: Dp = 140.dp,
    blink: Boolean = false,
) {
    val s = str()
    val light = pal().bg == Paper
    val context = LocalContext.current
    val open = remember(light) {
        val src = BitmapFactory.decodeResource(context.resources, R.drawable.logo)
        if (light) invertKeepingRed(src) else src
    }
    val shut = remember(open) { closeEye(open) }
    var closed by remember { mutableStateOf(false) }
    LaunchedEffect(blink) {
        closed = false
        if (!blink) return@LaunchedEffect
        while (true) {
            delay(Random.nextLong(900, 2200))
            closed = true
            delay(380)
            closed = false
            if (Random.nextFloat() < 0.35f) {
                delay(180)
                closed = true
                delay(280)
                closed = false
            }
        }
    }
    Image(
        bitmap = (if (closed) shut else open).asImageBitmap(),
        contentDescription = s.brandTitle,
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit,
    )
}

private fun invertKeepingRed(src: Bitmap): Bitmap {
    val out = src.copy(Bitmap.Config.ARGB_8888, true)
    val w = out.width
    val h = out.height
    val pixels = IntArray(w * h)
    out.getPixels(pixels, 0, w, 0, 0, w, h)
    for (i in pixels.indices) {
        val p = pixels[i]
        val a = p ushr 24
        val r = (p shr 16) and 0xFF
        val g = (p shr 8) and 0xFF
        val b = p and 0xFF
        if (isBlood(r, g, b)) continue
        pixels[i] = (a shl 24) or ((255 - r) shl 16) or ((255 - g) shl 8) or (255 - b)
    }
    out.setPixels(pixels, 0, w, 0, 0, w, h)
    return out
}

private fun closeEye(src: Bitmap): Bitmap {
    val out = src.copy(Bitmap.Config.ARGB_8888, true)
    val w = out.width
    val h = out.height
    val pixels = IntArray(w * h)
    out.getPixels(pixels, 0, w, 0, 0, w, h)
    val red = mutableListOf<Int>()
    var minY = h
    var maxY = 0
    for (i in pixels.indices) {
        val p = pixels[i]
        if (!isBlood((p shr 16) and 0xFF, (p shr 8) and 0xFF, p and 0xFF)) continue
        red.add(i)
        val y = i / w
        if (y < minY) minY = y
        if (y > maxY) maxY = y
    }
    if (red.isEmpty()) return out
    val eyeBottom = minY + ((maxY - minY) * 0.45f).toInt().coerceAtLeast(2)
    for (i in red) {
        val y = i / w
        if (y > eyeBottom) continue
        pixels[i] = lidColor(pixels, i, w)
    }
    out.setPixels(pixels, 0, w, 0, 0, w, h)
    return out
}

private fun lidColor(pixels: IntArray, i: Int, w: Int): Int {
    val y = i / w
    val x = i % w
    for (dx in intArrayOf(-2, -1, 1, 2, -3, 3, -4, 4)) {
        val nx = x + dx
        if (nx !in 0 until w) continue
        val p = pixels[y * w + nx]
        val r = (p shr 16) and 0xFF
        val g = (p shr 8) and 0xFF
        val b = p and 0xFF
        if ((p ushr 24) > 200 && !isBlood(r, g, b)) return p
    }
    return pixels[i] and 0xFF000000.toInt()
}

private fun isBlood(r: Int, g: Int, b: Int) = r > 150 && g < 80 && b < 80
