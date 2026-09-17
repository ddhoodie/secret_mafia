package com.secretmafia.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.secretmafia.game.Role
import com.secretmafia.ui.theme.pal

/**
 * Role art slot. Drop a white bust PNG at `res/drawable/role_<name>.png`.
 * Civilian: catalog uses the female art. In-game, `female = player.femaleArt`.
 */
@Composable
fun RolePortrait(
    role: Role,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    female: Boolean? = null,
) {
    val c = pal()
    val context = LocalContext.current
    val useFemale = role == Role.CIVILIAN && female != false
    val resName = if (useFemale) "role_civilian_f" else "role_${role.name.lowercase()}"
    val resId = remember(resName) {
        context.resources.getIdentifier(resName, "drawable", context.packageName)
    }
    Box(
        modifier = modifier
            .size(size)
            .border(2.dp, c.fg),
        contentAlignment = Alignment.Center,
    ) {
        if (resId != 0) {
            Image(
                painter = painterResource(resId),
                contentDescription = role.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                contentScale = ContentScale.Fit,
            )
        } else {
            BustSilhouette(Modifier.fillMaxSize().padding(10.dp))
        }
    }
}

@Composable
private fun BustSilhouette(modifier: Modifier = Modifier) {
    val color = pal().fg
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val headR = w * 0.22f
        val headCy = h * 0.30f
        drawCircle(color, headR, Offset(cx, headCy))
        val neckW = w * 0.12f
        val neckTop = headCy + headR * 0.72f
        val shouldersY = h * 0.58f
        drawPath(
            Path().apply {
                moveTo(cx - neckW / 2f, neckTop)
                lineTo(cx + neckW / 2f, neckTop)
                lineTo(cx + neckW / 2f, shouldersY)
                lineTo(cx - neckW / 2f, shouldersY)
                close()
            },
            color,
        )
        drawPath(
            Path().apply {
                moveTo(w * 0.06f, h * 0.96f)
                lineTo(w * 0.18f, shouldersY)
                quadraticTo(cx, h * 0.52f, w * 0.82f, shouldersY)
                lineTo(w * 0.94f, h * 0.96f)
                close()
            },
            color,
        )
    }
}
